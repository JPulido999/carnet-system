package com.zkteco.apizk.dto;

import java.time.LocalDateTime;

public record AccTransactionFiltroDTO(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String dni,
        String nombre,
        String departamento
) {}
