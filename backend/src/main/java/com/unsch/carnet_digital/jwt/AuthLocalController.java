package com.unsch.carnet_digital.jwt;

import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.dto.LoginRequest;
import com.unsch.carnet_digital.service.AuthLocalService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/auth/local")
public class AuthLocalController {

    private final AuthLocalService authLocalService;
    private final JwtService jwtService;

    public AuthLocalController(AuthLocalService authLocalService, JwtService jwtService) {
        this.authLocalService = authLocalService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = authLocalService.autenticar(
                request.username(),
                request.password()
        );

        String token = jwtService.generarToken(usuario);

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "rol", usuario.getRol(),
                        "nombres", usuario.getNombres()
                )
        );
    }
}
