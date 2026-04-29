package com.unsch.carnet_digital.usuario;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "codigo_estudiante", length = 20)
    private String codigoEstudiante;

    @Column(length = 150)
    private String escuela;

    @Column(name = "foto_carnet_url", columnDefinition = "TEXT")
    private String fotoCarnetUrl;

    @Column(name = "foto_google_url", columnDefinition = "TEXT")
    private String fotoGoogleUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RolUsuario rol;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_inicio_vigencia", nullable = false)
    private LocalDateTime fechaInicioVigencia;

    @Column(name = "fecha_fin_vigencia")
    private LocalDateTime fechaFinVigencia;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "uuid_verificacion", unique = true, length = 80)
    private String uuidVerificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_autenticacion", nullable = false, length = 20)
    private TipoAutenticacion tipoAutenticacion;

    /* ===============================
       🔥 NORMALIZACIÓN Y VALORES AUTOMÁTICOS
       =============================== */
    @PrePersist
    public void prePersist() {

        // 📌 Normalizar correo (CRÍTICO)
        if (this.correo != null) {
            this.correo = this.correo.trim().toLowerCase();
        }

        // 📌 Fecha creación automática
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }

        // 📌 UUID siempre generado
        if (this.uuidVerificacion == null || this.uuidVerificacion.isBlank()) {
            this.uuidVerificacion = UUID.randomUUID().toString();
        }

        // 📌 Seguridad mínima
        if (this.activo == false) {
            this.activo = true;
        }
    }

    /* ===============================
       🔥 NORMALIZACIÓN EN UPDATE
       =============================== */
    @PreUpdate
    public void preUpdate() {
        if (this.correo != null) {
            this.correo = this.correo.trim().toLowerCase();
        }
    }
}