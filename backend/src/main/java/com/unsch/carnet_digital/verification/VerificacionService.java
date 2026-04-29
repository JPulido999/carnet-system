package com.unsch.carnet_digital.verification;

import com.unsch.carnet_digital.common.ErrorCode;
import com.unsch.carnet_digital.foto.FotoService;
import com.unsch.carnet_digital.usuario.Usuario;
import com.unsch.carnet_digital.usuario.UsuarioRepository;
import com.unsch.carnet_digital.verification.dto.VerificacionUsuarioDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificacionService {

    private static final Logger logger = LoggerFactory.getLogger(VerificacionService.class);

    private final UsuarioRepository usuarioRepository;
    private final FotoService fotoService;

    public VerificacionService(UsuarioRepository usuarioRepository, FotoService fotoService) {
        this.usuarioRepository = usuarioRepository;
        this.fotoService = fotoService;
    }

    // ======================================
    // VERIFICACIÓN POR UUID
    // ======================================
    public VerificacionUsuarioDTO verificarPorUuid(String uuid) {

        if (uuid == null || uuid.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.INVALID_UUID.name()
            );
        }

        Usuario usuario = usuarioRepository.findByUuidVerificacion(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND.name()
                ));

        return mapToDTO(usuario);
    }

    // ======================================
    // VERIFICACIÓN MANUAL
    // ======================================
    public VerificacionUsuarioDTO verificarManual(String dni, String codigo) {

        if ((dni == null || dni.isBlank()) && (codigo == null || codigo.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.INVALID_INPUT.name()
            );
        }

        Usuario usuario;

        if (dni != null && !dni.isBlank()) {

            usuario = usuarioRepository.findByDni(dni.trim())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            ErrorCode.USER_NOT_FOUND.name()
                    ));

        } else {

            usuario = usuarioRepository.findByCodigoEstudiante(codigo.trim())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            ErrorCode.USER_NOT_FOUND.name()
                    ));
        }

        return mapToDTO(usuario);
    }

    // ======================================
    // MAPPER
    // ======================================
    private VerificacionUsuarioDTO mapToDTO(Usuario usuario) {

        String fotoBase64 = null;

        try {
            if (usuario.getFotoCarnetUrl() != null) {
                fotoBase64 = fotoService.cargarFotoBase64(usuario.getFotoCarnetUrl());
            }
        } catch (Exception e) {
            logger.warn("Error cargando foto para usuario ID={}", usuario.getId());
        }

        return new VerificacionUsuarioDTO(
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getDni(),
                usuario.getCodigoEstudiante(),
                usuario.getRol().name(),
                usuario.getEscuela(),
                fotoBase64
        );
    }
}