package com.unsch.carnet_digital.security.oauth;

import com.unsch.carnet_digital.security.jwt.JwtService;
import com.unsch.carnet_digital.usuario.Usuario;
import com.unsch.carnet_digital.usuario.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public OAuth2SuccessHandler(JwtService jwtService,
                               UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = (String) oAuth2User.getAttributes().get("email");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email no disponible");
            return;
        }
        email = email.toLowerCase().trim(); // 🔥 importante

        // 🔥 VALIDACIÓN EN BD
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoIgnoreCase(email);

        if (usuarioOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autorizado");
            return;
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.isActivo()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuario inactivo");
            return;
        }

        // 🔐 Generar token SOLO si está autorizado
        String token = jwtService.generateToken(email, usuario.getRol().name());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //response.getWriter().write("{\"token\": \"" + token + "\"}");
        String redirectUrl = frontendUrl.replaceAll("/$", "") + "/oauth-success?token=" + token;
        response.sendRedirect(redirectUrl);
        System.out.println("🔥 SUCCESS HANDLER EJECUTADO");
    }
}