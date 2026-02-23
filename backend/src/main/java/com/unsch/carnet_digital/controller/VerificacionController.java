package com.unsch.carnet_digital.controller;

import com.unsch.carnet_digital.dto.VerificacionUsuarioDTO;
import com.unsch.carnet_digital.service.VerificacionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verificacion")
public class VerificacionController {

    private final VerificacionService verificacionService;

    public VerificacionController(VerificacionService verificacionService) {
        this.verificacionService = verificacionService;
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('VIGILANTE')")
    public VerificacionUsuarioDTO verificarPorQr(@PathVariable String uuid) {
        return verificacionService.verificarPorUuid(uuid);
    }

    @GetMapping("/manual")
    @PreAuthorize("hasRole('VIGILANTE')")
    public VerificacionUsuarioDTO verificarManual(
        @RequestParam(required = false) String dni,
        @RequestParam(required = false) String codigo
    ) {
        return verificacionService.verificarManual(dni, codigo);
    }
}
