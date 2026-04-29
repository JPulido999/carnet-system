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

@Service
public class AuthLocalService {

    private static final Logger logger = LoggerFactory.getLogger(AuthLocalService.class);

    private final CredencialLocalRepository credencialRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthLocalService(CredencialLocalRepository credencialRepo,
                            PasswordEncoder passwordEncoder) {
        this.credencialRepo = credencialRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String username, String password) {

        String usernameNormalizado = username.trim().toLowerCase();

        CredencialLocal credencial = credencialRepo.findByUsername(usernameNormalizado)
                .orElseThrow(() -> {
                    logger.warn("LOGIN_FAIL: INVALID_CREDENTIALS - user={}", usernameNormalizado);
                    return new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            ErrorCode.INVALID_CREDENTIALS.name()
                    );
                });

        if (!credencial.isActivo()) {
            logger.warn("LOGIN_FAIL: CREDENTIAL_DISABLED - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ErrorCode.CREDENTIAL_DISABLED.name()
            );
        }

        if (!passwordEncoder.matches(password, credencial.getPasswordHash())) {
            logger.warn("LOGIN_FAIL: INVALID_CREDENTIALS - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    ErrorCode.INVALID_CREDENTIALS.name()
            );
        }

        Usuario usuario = credencial.getUsuario();

        if (usuario == null) {
            logger.error("LOGIN_ERROR: Usuario null - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCode.UNAUTHORIZED.name()
            );
        }

        if (!usuario.isActivo()) {
            logger.warn("LOGIN_FAIL: USER_INACTIVE - user={}", usernameNormalizado);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ErrorCode.USER_INACTIVE.name()
            );
        }

        logger.info("LOGIN_SUCCESS: user={}", usernameNormalizado);

        return usuario;
    }
}