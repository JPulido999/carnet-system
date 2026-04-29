package com.unsch.carnet_digital.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unsch.carnet_digital.auth.model.CredencialLocal;

import java.util.Optional;

public interface CredencialLocalRepository
        extends JpaRepository<CredencialLocal, Long> {

    Optional<CredencialLocal> findByUsername(String username);
}
