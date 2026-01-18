package com.docencia.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.tasks.entities.TaskEntity;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {}
