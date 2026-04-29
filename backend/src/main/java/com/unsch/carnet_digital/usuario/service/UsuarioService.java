package com.unsch.carnet_digital.usuario.service;

import com.unsch.carnet_digital.common.ErrorCode;
import com.unsch.carnet_digital.usuario.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioValidatorService validator;

    public UsuarioService(UsuarioRepository repository,
                          UsuarioValidatorService validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Usuario crear(Usuario usuario) {

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CORREO_REQUERIDO");
        }

        if (usuario.getDni() == null || usuario.getDni().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI_REQUERIDO");
        }

        if (usuario.getRol() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ROL_REQUERIDO");
        }

        usuario.setCorreo(normalizar(usuario.getCorreo()));

        // VALIDACIONES
        validator.validarCorreo(usuario.getCorreo());
        validator.validarDni(usuario.getDni());

        // VALIDAR DUPLICADOS (MUY IMPORTANTE)
        if (repository.existsByCorreoIgnoreCase(usuario.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CORREO_YA_EXISTE");
        }

        if (repository.existsByDni(usuario.getDni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI_YA_EXISTE");
        }

        inicializar(usuario);

        return repository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {

        Usuario u = buscarEntidad(id);

        u.setNombres(datos.getNombres());
        u.setApellidos(datos.getApellidos());

        u.setCorreo(normalizar(datos.getCorreo()));
        u.setDni(datos.getDni());

        u.setCodigoEstudiante(datos.getCodigoEstudiante());
        u.setEscuela(datos.getEscuela());
        u.setRol(datos.getRol());
        u.setActivo(datos.isActivo());

        return repository.save(u);
    }

    public Usuario buscarPorId(Long id) {
        return buscarEntidad(id);
    }

    public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreoIgnoreCase(normalizar(correo))
                .orElseThrow(this::notFound);
    }

    public Usuario buscarPorDni(String dni) {
        return repository.findByDni(dni)
                .orElseThrow(this::notFound);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    /* ========================= */

    private Usuario buscarEntidad(Long id) {
        return repository.findById(id)
                .orElseThrow(this::notFound);
    }

    private void inicializar(Usuario u) {

        u.setActivo(true);
        u.setFechaInicioVigencia(LocalDateTime.now());

        // ⚠ evita null en enum
        if (u.getTipoAutenticacion() == null) {
            u.setTipoAutenticacion(TipoAutenticacion.GOOGLE);
        }

        // UUID seguro
        if (u.getUuidVerificacion() == null || u.getUuidVerificacion().isBlank()) {
            u.setUuidVerificacion(UUID.randomUUID().toString());
        }

        if (u.getFechaCreacion() == null) {
            u.setFechaCreacion(LocalDateTime.now());
        }
    }

    private String normalizar(String correo) {
        return correo == null ? null : correo.trim().toLowerCase();
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                ErrorCode.USER_NOT_FOUND.name()
        );
    }
}