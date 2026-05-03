package com.unsch.carnet_digital.verification.dto;

public class VerificacionUsuarioDTO {

    private String nombres;
    private String apellidos;
    private String dni;
    private String codigoEstudiante;   
    private String rol;
    private String escuela;
    private String fotoBase64;
    private boolean activo;

    public VerificacionUsuarioDTO(
            String nombres,
            String apellidos,
            String dni,
            String codigoEstudiante,
            String rol,
            String escuela,
            String fotoBase64,
            Boolean activo
    ) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.codigoEstudiante = codigoEstudiante;
        this.rol = rol;
        this.escuela = escuela;
        this.fotoBase64 = fotoBase64;
        this.activo = activo;
    }

    // getters solamente (NO setters)
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getDni() { return dni; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public String getRol() { return rol; }
    public String getEscuela() { return escuela; }
    public String getFotoBase64() { return fotoBase64; }
    public boolean isActivo() { return activo; }
}
