package com.docencia.tasks.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;
/**
 * @author nexphernandez
 * @version 1.0.0
 * Interfaz jpa de Rol
 */
public interface RolJpaRepository extends JpaRepository<RolJpaEntity, Long>{
    Optional<RolJpaEntity> findByRol(String rol);
}
