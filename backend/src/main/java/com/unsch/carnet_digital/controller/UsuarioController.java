package com.unsch.carnet_digital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unsch.carnet_digital.model.Usuario;
import com.unsch.carnet_digital.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "${app.frontend.url}")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    /*@PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.crear(usuario));
    }*/

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Usuario> crear(
            @RequestParam("usuario") String usuarioJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Usuario usuario = mapper.readValue(usuarioJson, Usuario.class);

        return ResponseEntity.ok(service.crearConFoto(usuario, file));
    }

    @PutMapping(value = "/{id}/con-foto", consumes = "multipart/form-data")
    public ResponseEntity<Usuario> actualizarConFoto(
            @PathVariable Long id,
            @RequestParam("usuario") String usuarioJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Usuario usuario = mapper.readValue(usuarioJson, Usuario.class);

        return ResponseEntity.ok(service.actualizarConFoto(id, usuario, file));
    }

    @PostMapping("/import")
    public ResponseEntity<?> importarUsuarios(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fotos") List<MultipartFile> fotos
    ) {
        service.importar(file, fotos); // 🔥 FIX
        return ResponseEntity.ok("Importación exitosa");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.actualizar(id, usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(service.buscarPorCorreo(correo));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Usuario> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(service.buscarPorDni(dni));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @PostMapping("/importar")
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        service.importarCSV(file);
        return ResponseEntity.ok("Usuarios importados");
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Usuario>> listarPaginado(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listarPaginado(search, page, size));
    }
}