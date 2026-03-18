package com.unsch.carnet_digital.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class FotoService {

    private final String RUTA_FOTOS = "/app/uploads/";

    public String cargarFotoBase64(String nombreArchivo) {

        try {
            Path path = Path.of(RUTA_FOTOS + nombreArchivo);

            if (!Files.exists(path)) {
                return null;
            }

            byte[] bytes = Files.readAllBytes(path);

            return Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
