package com.unsch.carnet_digital.security.jwt;

import com.unsch.carnet_digital.usuario.Usuario;
import com.unsch.carnet_digital.usuario.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔥 1. BYPASS DE ENDPOINTS PÚBLICOS
        if (path.startsWith("/auth")
                || path.startsWith("/oauth2")
                || path.startsWith("/login")
                || path.startsWith("/error")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 2. SI YA ESTÁ AUTENTICADO, CONTINÚA
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 3. EXTRAER TOKEN
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 🔥 4. VALIDAR TOKEN
        if (!jwtService.isValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        // 🔥 5. EXTRAER EMAIL
        String email = jwtService.extractEmail(token);

        if (email == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token sin email");
            return;
        }

        // 🔥 6. BUSCAR USUARIO
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoIgnoreCase(email);

        if (usuarioOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no registrado");
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // 🔥 7. VALIDAR ESTADO
        if (!usuario.isActivo()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuario inactivo");
            return;
        }

        // 🔥 8. CREAR AUTENTICACIÓN
        String rol = jwtService.extractRol(token);

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + rol));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        // 🔥 9. CONTINUAR FILTRO
        filterChain.doFilter(request, response);
    }
}