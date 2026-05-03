package com.unsch.carnet_digital.auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificacionLogRepository
        extends JpaRepository<VerificacionLog, Long> {
}
