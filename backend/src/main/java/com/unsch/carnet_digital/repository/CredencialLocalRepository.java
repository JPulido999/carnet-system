package com.unsch.carnet_digital.repository;

import com.unsch.carnet_digital.model.CredencialLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredencialLocalRepository
        extends JpaRepository<CredencialLocal, Long> {

    Optional<CredencialLocal> findByUsername(String username);
}
