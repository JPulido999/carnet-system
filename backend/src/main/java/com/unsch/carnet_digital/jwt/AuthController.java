package com.unsch.carnet_digital.jwt;

import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.repository.UsuarioRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/loginSuccess")
    public void loginSuccess(@AuthenticationPrincipal OAuth2User user,
                             HttpServletResponse response) throws IOException {

        if (user == null) {
        response.sendRedirect("http://localhost:5173/login?error=OAUTH_ERROR");
        return;
        }       
        
        String correo = null;

        if (user != null && user.getAttributes() != null) {
            Object o = user.getAttributes().get("email");
            if (o != null) correo = o.toString();
            else {
                o = user.getAttributes().get("emailAddress");
                if (o != null) correo = o.toString();
            }
        }

        if (correo == null) {
            response.sendRedirect("http://localhost:5173/login?error=NO_EMAIL");
            return;
        }

        var usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            response.sendRedirect("http://localhost:5173/login?error=NO_REGISTRADO");
            return;
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.isActivo()) {
            response.sendRedirect("http://localhost:5173/login?error=INACTIVO");
            return;
        }

        // Generar JWT incluyendo el rol del usuario
        String token = jwtService.generarToken(usuario);

        // Redirigir al frontend
        response.sendRedirect("http://localhost:5173/?token=" + token);
    }
}
