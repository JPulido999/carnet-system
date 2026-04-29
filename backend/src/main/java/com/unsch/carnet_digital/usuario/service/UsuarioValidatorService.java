package com.unsch.carnet_digital.usuario.service;

import com.unsch.carnet_digital.common.ErrorCode;
import com.unsch.carnet_digital.usuario.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioValidatorService {

    private final UsuarioRepository repository;

    public UsuarioValidatorService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void validarCorreo(String correo) {
        if (repository.existsByCorreoIgnoreCase(correo)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    ErrorCode.USER_ALREADY_EXISTS.name()
            );
        }
    }

    public void validarDni(String dni) {
        if (repository.existsByDni(dni)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    ErrorCode.USER_ALREADY_EXISTS.name()
            );
        }
    }
}