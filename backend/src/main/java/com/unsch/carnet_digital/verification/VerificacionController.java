package com.unsch.carnet_digital.verification;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.unsch.carnet_digital.verification.dto.VerificacionUsuarioDTO;

@RestController
@RequestMapping("/verificacion")
public class VerificacionController {

    private final VerificacionService verificacionService;

    public VerificacionController(VerificacionService verificacionService) {
        this.verificacionService = verificacionService;
    }

    // ======================================
    // VERIFICACIÓN QR (UUID)
    // ======================================
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('VIGILANTE', 'ADMIN_SISTEMA')")
    public VerificacionUsuarioDTO verificarPorQr(@PathVariable String uuid) {
        return verificacionService.verificarPorUuid(uuid);
    }

    // ======================================
    // VERIFICACIÓN MANUAL
    // ======================================
    @GetMapping("/manual")
    @PreAuthorize("hasRole('VIGILANTE', 'ADMIN_SISTEMA')")
    public VerificacionUsuarioDTO verificarManual(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String codigo
    ) {
        return verificacionService.verificarManual(dni, codigo);
    }
}