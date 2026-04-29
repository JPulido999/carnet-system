package com.unsch.carnet_digital.auth;

import com.unsch.carnet_digital.auth.dto.LoginRequest;
import com.unsch.carnet_digital.auth.dto.AuthResponse;
import com.unsch.carnet_digital.auth.service.AuthLocalService;
import com.unsch.carnet_digital.security.jwt.JwtService;
import com.unsch.carnet_digital.usuario.Usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthLocalService authLocalService;
    private final JwtService jwtService;

    public AuthController(AuthLocalService authLocalService, JwtService jwtService) {
        this.authLocalService = authLocalService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        Usuario usuario = authLocalService.autenticar(
                request.username(),
                request.password()
        );

        String token = jwtService.generateToken(
                usuario.getCorreo(),
                usuario.getRol().name()
        );

        AuthResponse response = new AuthResponse(
                token,
                usuario.getRol().name(),
                usuario.getNombres()
        );

        return ResponseEntity.ok(response);
    }
}