package com.unsch.carnet_digital.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "credencial_local")
public class CredencialLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean activo = true;
}
