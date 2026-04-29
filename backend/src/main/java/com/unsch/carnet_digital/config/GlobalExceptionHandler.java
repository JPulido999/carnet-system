package com.unsch.carnet_digital.config;

import com.unsch.carnet_digital.common.ErrorCode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🔴 Manejo de errores controlados (los que tú lanzas)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handle(ResponseStatusException ex) {

        String code = ex.getReason() != null
                ? ex.getReason()
                : ErrorCode.UNAUTHORIZED.name();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of(
                        "code", code,
                        "status", ex.getStatusCode().value()
                ));
    }

    // 🔥 Manejo de errores inesperados (muy importante)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {

        // Log real para backend (esto SÍ lo ves tú)
        logger.error("ERROR NO CONTROLADO", ex);

        return ResponseEntity
                .status(500)
                .body(Map.of(
                        "code", ErrorCode.INTERNAL_ERROR.name(), // puedes cambiar a INTERNAL_ERROR si lo agregas al enum
                        "status", 500
                ));
    }
}