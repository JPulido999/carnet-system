package com.unsch.carnet_digital.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    Optional<Usuario> findByDni(String dni);

    Optional<Usuario> findByUuidVerificacion(String uuidVerificacion);

    Optional<Usuario> findByCodigoEstudiante(String codigo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByDni(String dni);

    Page<Usuario> findByDniContainingIgnoreCaseOrNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String dni, String nombres, String apellidos, Pageable pageable
    );
}