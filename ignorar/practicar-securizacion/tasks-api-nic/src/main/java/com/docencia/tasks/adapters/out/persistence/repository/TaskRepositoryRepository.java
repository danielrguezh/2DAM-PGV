package com.docencia.tasks.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.tasks.adapters.out.persistence.jpa.TaskJpaEntity;
/**
 * @author nexphernandez
 * @version 1.0.0
 * Interfaz jpa de task
 */
public interface TaskRepositoryRepository extends JpaRepository<TaskJpaEntity, Long> {
}
