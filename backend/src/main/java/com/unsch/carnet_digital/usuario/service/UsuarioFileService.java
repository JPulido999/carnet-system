package com.unsch.carnet_digital.usuario.service;

import com.unsch.carnet_digital.common.ErrorCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.*;

@Service
public class UsuarioFileService {

    @Value("${app.upload.dir:/app/uploads/}")
    private String uploadDir;

    public String guardarFoto(String dni, MultipartFile file) {

        try {
            String nombre = dni + ".jpg";

            Path ruta = Paths.get(uploadDir + nombre);

            Files.createDirectories(ruta.getParent());

            Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

            return nombre;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCode.FILE_UPLOAD_ERROR.name()
            );
        }
    }
}