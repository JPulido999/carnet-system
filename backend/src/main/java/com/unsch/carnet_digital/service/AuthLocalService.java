package com.unsch.carnet_digital.service;

import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.model.CredencialLocal;
import com.unsch.carnet_digital.repository.CredencialLocalRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthLocalService {

    private final CredencialLocalRepository credencialRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthLocalService(CredencialLocalRepository credencialRepo,
                            PasswordEncoder passwordEncoder) {
        this.credencialRepo = credencialRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String username, String password) {

        CredencialLocal credencial = credencialRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!credencial.isActivo()) {
            throw new RuntimeException("Credencial deshabilitada");
        }

        if (!passwordEncoder.matches(password, credencial.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        Usuario usuario = credencial.getUsuario();

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return usuario;
    }
}
