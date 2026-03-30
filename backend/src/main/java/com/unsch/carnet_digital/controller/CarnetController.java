package com.unsch.carnet_digital.controller;

import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.service.BarcodeService;
import com.unsch.carnet_digital.service.FotoService;
import com.unsch.carnet_digital.service.QRService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/carnet")
public class CarnetController {

    private final FotoService fotoService;
    private final BarcodeService barcodeService;
    private final QRService qrService;
    

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public CarnetController(BarcodeService barcodeService, QRService qrService, FotoService fotoService) {
        this.barcodeService = barcodeService;
        this.qrService = qrService;
        this.fotoService = fotoService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerCarnet(@AuthenticationPrincipal Usuario usuario) {

        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        try {
            /* ===============================
               1. Código carnet (estudiante o vigilante)
               =============================== */
            String codigoCarnet =
                    (usuario.getCodigoEstudiante() != null && !usuario.getCodigoEstudiante().isBlank())
                            ? usuario.getCodigoEstudiante()
                            : usuario.getDni();

            if (codigoCarnet == null) {
                throw new RuntimeException("Usuario sin DNI ni código");
            }

            /* ===============================
               2. UUID verificación
               =============================== */
            if (usuario.getUuidVerificacion() == null) {
                usuario.setUuidVerificacion(UUID.randomUUID().toString());
            }

            /* ===============================
               3. Foto carnet
               =============================== */
            String nombreFoto =
                    (usuario.getFotoCarnetUrl() != null && !usuario.getFotoCarnetUrl().isBlank())
                            ? usuario.getFotoCarnetUrl()
                            : "default.jpg";

            String fotoBase64 = fotoService.cargarFotoBase64(nombreFoto);

            /* ===============================
               4. Generar códigos
               =============================== */
            String barcodeBase64 =
                    barcodeService.generarCode128Base64(codigoCarnet, 600, 150);

            String urlVerificacion =
                    frontendUrl + "/verificacion/" + usuario.getUuidVerificacion();

            String qrBase64 =
                    qrService.generarQRCodeBase64(urlVerificacion, 260, 260);

            /* ===============================
               5. RESPUESTA SEGURA (SIN Map.of)
               =============================== */
            Map<String, Object> data = new HashMap<>();

            data.put("id", usuario.getId());
            data.put("nombres", usuario.getNombres());
            data.put("apellidos", usuario.getApellidos());
            data.put("dni", usuario.getDni());
            data.put("correo", usuario.getCorreo());
            data.put("codigoEstudiante", codigoCarnet);
            data.put("escuela", usuario.getEscuela()); // puede ser null
            data.put("fotoBase64", fotoBase64);
            data.put("barcodeBase64", barcodeBase64);
            data.put("qrBase64", qrBase64);

            return ResponseEntity.ok(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
