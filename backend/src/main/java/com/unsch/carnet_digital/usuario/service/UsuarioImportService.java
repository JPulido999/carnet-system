package com.unsch.carnet_digital.usuario.service;

import com.unsch.carnet_digital.common.ErrorCode;
import com.unsch.carnet_digital.usuario.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class UsuarioImportService {

    private final UsuarioRepository repository;

    public UsuarioImportService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void importarCSV(MultipartFile file) {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");

                if (d.length < 4) continue;

                Usuario u = new Usuario();
                u.setDni(d[0]);
                u.setNombres(d[1]);
                u.setApellidos(d[2]);
                u.setCorreo(d[3]);
                u.setRol(RolUsuario.ESTUDIANTE);

                repository.save(u);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.IMPORT_ERROR.name()
            );
        }
    }

    public List<Usuario> leerExcel(MultipartFile file) {

        List<Usuario> usuarios = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Usuario u = new Usuario();

                u.setDni(row.getCell(0).toString());
                u.setNombres(row.getCell(1).toString());
                u.setApellidos(row.getCell(2).toString());
                u.setCorreo(row.getCell(3).toString());
                u.setRol(RolUsuario.ESTUDIANTE);

                usuarios.add(u);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.IMPORT_ERROR.name()
            );
        }

        return usuarios;
    }
}