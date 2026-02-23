package com.unsch.carnet_digital.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

@Service
public class BarcodeService {

    // CODE 128
    public String generarCode128Base64(String texto, int width, int height) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(texto, BarcodeFormat.CODE_128, width, height);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    public String generarCode128Base64(String texto) throws Exception {
        return generarCode128Base64(texto, 600, 150);
    }

    // QR opcional (si deseas mantenerlo, pero yo recomiendo separarlo)
}
