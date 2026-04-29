package com.unsch.carnet_digital.auth.dto;

public class AuthResponse {

    private String token;
    private String rol;
    private String nombres;

    public AuthResponse(String token, String rol, String nombres) {
        this.token = token;
        this.rol = rol;
        this.nombres = nombres;
    }

    public String getToken() {
        return token;
    }

    public String getRol() {
        return rol;
    }

    public String getNombres() {
        return nombres;
    }
}