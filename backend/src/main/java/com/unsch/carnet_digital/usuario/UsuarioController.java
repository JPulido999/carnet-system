package com.unsch.carnet_digital.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unsch.carnet_digital.usuario.service.UsuarioFacadeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "${app.frontend.url}")
public class UsuarioController {

    private final UsuarioFacadeService service;

    public UsuarioController(UsuarioFacadeService service) {
        this.service = service;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Usuario> crear(
            @RequestPart("usuario") Usuario usuario,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {

        return ResponseEntity.ok(
                service.crearConFoto(usuario, file)
        );
    }

    @GetMapping("/ping")
    public String ping() {
        return "FUNCIONA";
    }
    // ======================================
    // ACTUALIZAR CON FOTO
    // ======================================
    @PutMapping(value = "/{id}/con-foto", consumes = "multipart/form-data")
    public ResponseEntity<Usuario> actualizarConFoto(
            @PathVariable Long id,
            @RequestParam("usuario") String usuarioJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        Usuario usuario = new ObjectMapper()
                .readValue(usuarioJson, Usuario.class);

        return ResponseEntity.ok(
                service.actualizarConFoto(id, usuario, file)
        );
    }

    // ======================================
    // IMPORTACIÓN (EXCEL + FOTOS)
    // ======================================
    @PostMapping("/import")
    public ResponseEntity<String> importarUsuarios(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fotos") List<MultipartFile> fotos
    ) {
        service.importar(file, fotos);
        return ResponseEntity.ok("Importación exitosa");
    }

    // ======================================
    // ACTUALIZAR SIMPLE
    // ======================================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario
    ) {
        return ResponseEntity.ok(
                service.actualizarConFoto(id, usuario, null)
        );
    }

    // ======================================
    // CONSULTAS
    // ======================================
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

    @GetMapping("/page")
    public ResponseEntity<Page<Usuario>> listarPaginado(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                service.listarPaginado(search, page, size)
        );
    }

    // ======================================
    // ELIMINAR
    // ======================================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}