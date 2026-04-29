package com.unsch.carnet_digital.security.user;

import com.unsch.carnet_digital.auth.model.CredencialLocal;
import com.unsch.carnet_digital.auth.repository.CredencialLocalRepository;
import com.unsch.carnet_digital.usuario.Usuario;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CredencialLocalRepository credencialRepository;

    public CustomUserDetailsService(CredencialLocalRepository credencialRepository) {
        this.credencialRepository = credencialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CredencialLocal credencial = credencialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales no encontradas"));

        Usuario usuario = credencial.getUsuario();

        String rolSpring = "ROLE_" + usuario.getRol().name();

        return new User(
                credencial.getUsername(),
                credencial.getPasswordHash(), // 🔥 contraseña REAL
                usuario.isActivo(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(rolSpring))
        );
    }
}