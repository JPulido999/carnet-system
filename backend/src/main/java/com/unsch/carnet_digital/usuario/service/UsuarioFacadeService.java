package com.unsch.carnet_digital.usuario.service;

import com.unsch.carnet_digital.usuario.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UsuarioFacadeService {

    private final UsuarioService usuarioService;
    private final UsuarioFileService fileService;
    private final UsuarioImportService importService;

    public UsuarioFacadeService(
            UsuarioService usuarioService,
            UsuarioFileService fileService,
            UsuarioImportService importService
    ) {
        this.usuarioService = usuarioService;
        this.fileService = fileService;
        this.importService = importService;
    }

    // ======================================
    // CREATE CON FOTO
    // ======================================
    public Usuario crearConFoto(Usuario usuario, MultipartFile file) {

        Usuario saved = usuarioService.crear(usuario);

        if (file != null && !file.isEmpty()) {
            String foto = fileService.guardarFoto(saved.getDni(), file);
            saved.setFotoCarnetUrl(foto);
            saved = usuarioService.actualizar(saved.getId(), saved);
        }

        return saved;
    }

    // ======================================
    // UPDATE CON FOTO
    // ======================================
    public Usuario actualizarConFoto(Long id, Usuario usuario, MultipartFile file) {

        Usuario actualizado = usuarioService.actualizar(id, usuario);

        if (file != null && !file.isEmpty()) {
            String foto = fileService.guardarFoto(actualizado.getDni(), file);
            actualizado.setFotoCarnetUrl(foto);
            return usuarioService.actualizar(id, actualizado);
        }

        return actualizado;
    }

    // ======================================
    // IMPORTACIÓN
    // ======================================
    public void importar(MultipartFile file, List<MultipartFile> fotos) {
        importService.importarCSV(file);
    }

    // ======================================
    // CRUD DIRECTO (ESTO FALTABA)
    // ======================================

    public Usuario buscarPorId(Long id) {
        return usuarioService.buscarPorId(id);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }

    public Usuario buscarPorDni(String dni) {
        return usuarioService.buscarPorDni(dni);
    }

    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    public void eliminar(Long id) {
        usuarioService.eliminar(id);
    }

    // ======================================
    // PAGINACIÓN
    // ======================================
    public Page<Usuario> listarPaginado(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<Usuario> all = usuarioService.listar();

        List<Usuario> filtered = all.stream()
                .filter(u -> search == null || search.isBlank()
                        || u.getNombres().toLowerCase().contains(search.toLowerCase())
                        || u.getApellidos().toLowerCase().contains(search.toLowerCase()))
                .toList();

        int start = Math.min((int) pageable.getOffset(), filtered.size());
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        return new PageImpl<>(
                filtered.subList(start, end),
                pageable,
                filtered.size()
        );
    }
}