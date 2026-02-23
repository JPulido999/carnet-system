package com.unsch.carnet_digital.service;

import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crear(Usuario usuario) {

        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        if (usuario.getDni() != null && repository.existsByDni(usuario.getDni())) {
            throw new RuntimeException("El DNI ya está registrado.");
        }

        usuario.setActivo(true);

        // ✅ UUID SIEMPRE
        usuario.setUuidVerificacion(UUID.randomUUID().toString());

        return repository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombres(datos.getNombres());
        usuario.setApellidos(datos.getApellidos());
        usuario.setDni(datos.getDni());
        usuario.setCorreo(datos.getCorreo());
        usuario.setCodigoEstudiante(datos.getCodigoEstudiante());
        usuario.setEscuela(datos.getEscuela());
        usuario.setFotoCarnetUrl(datos.getFotoCarnetUrl());
        usuario.setFotoGoogleUrl(datos.getFotoGoogleUrl());
        usuario.setRol(datos.getRol());
        usuario.setActivo(datos.isActivo());

        return repository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe usuario con ese correo"));
    }

    public Usuario buscarActivoPorCorreo(String correo) {

        Usuario usuario = repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no registrado."));

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo.");
        }

        return usuario;
    }

    public Usuario buscarPorDni(String dni) {
        return repository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("No existe usuario con ese DNI"));
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
