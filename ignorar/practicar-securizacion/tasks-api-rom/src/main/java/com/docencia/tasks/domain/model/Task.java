package com.docencia.tasks.domain.model;

import java.util.Objects;

/**
 * Clase task
 * @author prorix
 * @version 1.0.0
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private boolean completed;

    /**
     * Constructor vacio
     */
    public Task() {
    }

    /**
     * Consturctor con solo la clave primaria
     *
     * @param id id de la tarea
     */
    public Task(Long id) {
        this.id = id;
    }

    /**
     * Constructor con todos los parametros
     *
     * @param id          id de la tarea
     * @param title       titulo de la tarea
     * @param description descripcion de la tarea
     * @param completed   si la tarea esta completa o no
     */
    public Task(Long id, String title, String description, boolean completed) {
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
