package com.docencia.tasks.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.tasks.adapters.out.persistence.jpa.TaskJpaEntity;

/**
 * Interfaz jpa de task
 * @author prorix
 * @version 1.0.0
 */
public interface TaskRepositoryRepository extends JpaRepository<TaskJpaEntity, Long> {
}
