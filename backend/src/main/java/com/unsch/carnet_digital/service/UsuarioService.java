package com.unsch.carnet_digital.service;

import com.unsch.carnet_digital.model.RolUsuario;
import com.unsch.carnet_digital.model.TipoAutenticacion;
import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.repository.UsuarioRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.Map;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crear(Usuario usuario) {

        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        if (usuario.getDni() != null && repository.existsByDni(usuario.getDni())) {
            throw new RuntimeException("El DNI ya está registrado.");
        }

        usuario.setActivo(true);

        // ✅ VALORES AUTOMÁTICOS
        usuario.setFechaInicioVigencia(LocalDateTime.now());
        usuario.setTipoAutenticacion(TipoAutenticacion.GOOGLE);

        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(LocalDateTime.now());
        }

        // ✅ UUID SIEMPRE
        usuario.setUuidVerificacion(UUID.randomUUID().toString());

        return repository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombres(datos.getNombres());
        usuario.setApellidos(datos.getApellidos());
        usuario.setDni(datos.getDni());
        usuario.setCorreo(datos.getCorreo());
        usuario.setCodigoEstudiante(datos.getCodigoEstudiante());
        usuario.setEscuela(datos.getEscuela());
        usuario.setFotoCarnetUrl(datos.getFotoCarnetUrl());
        usuario.setFotoGoogleUrl(datos.getFotoGoogleUrl());
        usuario.setRol(datos.getRol());
        usuario.setActivo(datos.isActivo());

        return repository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe usuario con ese correo"));
    }

    public Usuario buscarActivoPorCorreo(String correo) {

        Usuario usuario = repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no registrado."));

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo.");
        }

        return usuario;
    }

    public Usuario buscarPorDni(String dni) {
        return repository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("No existe usuario con ese DNI"));
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public void importarCSV(MultipartFile file) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                Usuario u = new Usuario();
                u.setDni(data[0]);
                u.setNombres(data[1]);
                u.setApellidos(data[2]);
                u.setCorreo(data[3]);
                u.setRol(RolUsuario.ESTUDIANTE);

                repository.save(u);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al importar CSV");
        }
    }

    public Page<Usuario> listarPaginado(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (search == null || search.isBlank()) {
            return repository.findAll(pageable);
        }

        return repository.findByDniContainingOrNombresContainingOrApellidosContaining(
                search, search, search, pageable
        );
    }

    public Usuario crearConFoto(Usuario usuario, MultipartFile file) {

        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        usuario.setActivo(true);
        usuario.setFechaInicioVigencia(LocalDateTime.now());
        usuario.setTipoAutenticacion(TipoAutenticacion.GOOGLE);
        usuario.setUuidVerificacion(UUID.randomUUID().toString());

        // 📸 GUARDAR FOTO
        if (file != null && !file.isEmpty()) {
            try {
                String nombreArchivo = usuario.getDni() + ".jpg";

                Path ruta = Paths.get("/app/uploads/" + nombreArchivo);

                Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

                usuario.setFotoCarnetUrl(nombreArchivo);

            } catch (Exception e) {
                throw new RuntimeException("Error guardando imagen");
            }
        }

        return repository.save(usuario);
    }

    public Usuario actualizarConFoto(Long id, Usuario datos, MultipartFile file) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombres(datos.getNombres());
        usuario.setApellidos(datos.getApellidos());
        usuario.setDni(datos.getDni());
        usuario.setCorreo(datos.getCorreo());
        usuario.setCodigoEstudiante(datos.getCodigoEstudiante());
        usuario.setEscuela(datos.getEscuela());
        usuario.setRol(datos.getRol());
        usuario.setActivo(datos.isActivo());

        // 📸 SI VIENE FOTO
        if (file != null && !file.isEmpty()) {
            try {
                String nombreArchivo = usuario.getDni() + ".jpg";

                Path ruta = Paths.get("uploads/" + nombreArchivo);

                Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

                usuario.setFotoCarnetUrl(nombreArchivo);

            } catch (Exception e) {
                throw new RuntimeException("Error actualizando imagen");
            }
        }

        return repository.save(usuario);
    }

    public void importar(MultipartFile file, List<MultipartFile> fotos) {

        try {
            Map<String, MultipartFile> mapaFotos = new HashMap<>();

            // 📸 Mapear fotos por DNI
            for (MultipartFile foto : fotos) {
                String nombre = foto.getOriginalFilename(); // 12345678.jpg
                if (nombre == null) continue;

                String dni = nombre.split("\\.")[0];
                mapaFotos.put(dni, foto);
            }

            List<Usuario> usuarios = leerExcel(file);

            for (Usuario u : usuarios) {

                // VALIDACIONES BÁSICAS
                if (repository.existsByDni(u.getDni())) continue;

                MultipartFile foto = mapaFotos.get(u.getDni());

                if (foto != null && !foto.isEmpty()) {
                    String nombreArchivo = u.getDni() + ".jpg";

                    Path ruta = Paths.get("uploads/" + nombreArchivo);
                    Files.copy(foto.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

                    u.setFotoCarnetUrl("uploads/" + nombreArchivo);
                }

                u.setActivo(true);
                u.setFechaInicioVigencia(LocalDateTime.now());
                u.setTipoAutenticacion(TipoAutenticacion.GOOGLE);
                u.setUuidVerificacion(UUID.randomUUID().toString());

                repository.save(u);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error importando usuarios", e);
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

                u.setDni(getCellValue(row.getCell(0)));
                u.setNombres(getCellValue(row.getCell(1)));
                u.setApellidos(getCellValue(row.getCell(2)));
                u.setCorreo(getCellValue(row.getCell(3)));

                // 🔥 Rol por defecto
                u.setRol(RolUsuario.ESTUDIANTE);

                usuarios.add(u);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo Excel", e);
        }

        return usuarios;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return "";
        }
    }
    
}
