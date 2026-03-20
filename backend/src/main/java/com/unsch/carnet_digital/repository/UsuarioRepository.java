package com.unsch.carnet_digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.unsch.carnet_digital.model.Usuario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByDni(String dni);

    Optional<Usuario> findByUuidVerificacion(String uuidVerificacion);

    Optional<Usuario> findByCodigoEstudiante(String codigo);

    boolean existsByCorreo(String correo);

    boolean existsByDni(String dni);

    Page<Usuario> findByDniContainingOrNombresContainingOrApellidosContaining(
    String dni, String nombres, String apellidos, Pageable pageable
);
}