package com.unsch.carnet_digital.auth.service;

import com.unsch.carnet_digital.auth.model.CredencialLocal;
import com.unsch.carnet_digital.auth.repository.CredencialLocalRepository;
import com.unsch.carnet_digital.usuario.Usuario;
import com.unsch.carnet_digital.common.ErrorCode;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthLocalService {

    private static final Logger logger = LoggerFactory.getLogger(AuthLocalService.class);

    private final CredencialLocalRepository credencialRepo;
    private final PasswordEncoder passwordEncoder;

    private final ConcurrentHashMap<String, Integer> intentosFallidos = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> bloqueadosHasta = new ConcurrentHashMap<>();

    private static final int MAX_INTENTOS = 5;
    private static final long BLOQUEO_MS = 10 * 60 * 1000; // 10 min

    public AuthLocalService(CredencialLocalRepository credencialRepo,
                            PasswordEncoder passwordEncoder) {
        this.credencialRepo = credencialRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String username, String password) {

        String usernameNormalizado = username.trim().toLowerCase();
        
        // 🔥 1. VERIFICAR BLOQUEO
        if (bloqueadosHasta.containsKey(usernameNormalizado)) {
            long tiempo = bloqueadosHasta.get(usernameNormalizado);

            if (System.currentTimeMillis() < tiempo) {
                logger.warn("LOGIN_BLOCKED: user={}", usernameNormalizado);
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        ErrorCode.USER_BLOCKED.name()
                );
            } else {
                // 🔓 desbloquear
                bloqueadosHasta.remove(usernameNormalizado);
                intentosFallidos.remove(usernameNormalizado);
            }
        }

        // 🔍 2. BUSCAR CREDENCIAL
        CredencialLocal credencial = credencialRepo.findByUsername(usernameNormalizado)
                .orElseThrow(() -> {
                    registrarIntentoFallido(usernameNormalizado);
                    logger.warn("LOGIN_FAIL: INVALID_CREDENTIALS - user={}", usernameNormalizado);
                    return new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            ErrorCode.INVALID_CREDENTIALS.name()
                    );
                });

        // 🚫 3. VALIDAR CREDENCIAL ACTIVA
        if (!credencial.isActivo()) {
            logger.warn("LOGIN_FAIL: CREDENTIAL_DISABLED - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ErrorCode.CREDENTIAL_DISABLED.name()
            );
        }

        // 🔐 4. VALIDAR PASSWORD              
        if (!passwordEncoder.matches(password, credencial.getPasswordHash())) {
            registrarIntentoFallido(usernameNormalizado);
            logger.warn("LOGIN_FAIL: INVALID_CREDENTIALS - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    ErrorCode.INVALID_CREDENTIALS.name()
            );
        }

        Usuario usuario = credencial.getUsuario();

        // ❗ 5. VALIDACIÓN INTERNA
        if (usuario == null) {
            logger.error("LOGIN_ERROR: Usuario null - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCode.UNAUTHORIZED.name()
            );
        }

        // 🚫 6. VALIDAR USUARIO ACTIVO
        if (!usuario.isActivo()) {
            logger.warn("LOGIN_FAIL: USER_INACTIVE - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ErrorCode.USER_INACTIVE.name()
            );
        }

         // ✅ 7. LIMPIAR INTENTOS FALLIDOS (CLAVE)
        intentosFallidos.remove(usernameNormalizado);
        bloqueadosHasta.remove(usernameNormalizado);

        logger.info("LOGIN_SUCCESS: user={}", usernameNormalizado);

        return usuario;
    }
    
    // 🔥 MÉTODO CENTRALIZADO
    private void registrarIntentoFallido(String username) {
        int intentos = intentosFallidos.getOrDefault(username, 0) + 1;
        intentosFallidos.put(username, intentos);

        if (intentos >= MAX_INTENTOS) {
            bloqueadosHasta.put(username, System.currentTimeMillis() + BLOQUEO_MS);
            logger.warn("USER_BLOCKED: user={} intentos={}", username, intentos);
        }
    }
}