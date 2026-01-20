package com.docencia.tasks.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;

/**
 * Interfaz jpa de rol
 * @author prorix
 * @version 1.0.0
 */
public interface RolJpaRepository extends JpaRepository<RolJpaEntity, Long> {
    /**
     * Metodo que busca un rol por el nombre
     * @param rol nombre del rol
     * @return rol encontrado (o null)
     */
    Optional<RolJpaEntity> findByRol(String rol);
}
