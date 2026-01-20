package com.docencia.tasks.adapters.out.persistence.jpa;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Clase jpa de task
 * @author prorix
 * @version 1.0.0
 */
@Entity
@Table(name = "tasks")
public class TaskJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private boolean completed;

    /**
     * Contructor vacio
     */
    public TaskJpaEntity() {
    }

    /**
     * Constuctor con solo la clave primaria
     * 
     * @param id id de la tarea
     */
    public TaskJpaEntity(Long id) {
        this.id = id;
    }

    /**
     * Constructor con todos los parametros
     *
     * @param id          id de la tarea
     * @param title       titulo de la tarea
     * @param description descripcion de la tarea
     * @param completed   si esta completada o no
     */
    public TaskJpaEntity(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TaskJpaEntity)) {
            return false;
        }
        TaskJpaEntity taskJpaEntity = (TaskJpaEntity) o;
        return Objects.equals(id, taskJpaEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
