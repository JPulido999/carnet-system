package com.unsch.carnet_digital.auditoria;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.unsch.carnet_digital.usuario.Usuario;
import lombok.Data;

@Data
@Entity
@Table(name = "verificacion_log")
public class VerificacionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 Usuario verificado (estudiante)
    @ManyToOne
    @JoinColumn(name = "usuario_verificado_id")
    private Usuario usuarioVerificado;

    // 🛡️ Quién hizo la verificación (vigilante)
    @ManyToOne
    @JoinColumn(name = "verificado_por_id")
    private Usuario verificadoPor;

    // 🔍 Tipo de verificación
    @Enumerated(EnumType.STRING)
    private MetodoVerificacion metodo;

    // 📌 Valor buscado (uuid, dni, etc)
    private String valorBuscado;

    // 🕒 Fecha
    private LocalDateTime fecha;

    // 🌐 IP
    private String ip;
}