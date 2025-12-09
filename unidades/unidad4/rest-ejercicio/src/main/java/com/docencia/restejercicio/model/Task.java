package com.docencia.restejercicio.model;
import java.util.Objects;

/**
 * @author danielrguezh 
 * @version 1.0.0
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private boolean done;

    /**
     * Constructor vacio
     */
    public Task() {
    }
    /**
     * Constructor con el identificador
     * @param id unico de Task
     */
    public Task(Long id) {
        this.id = id;
    }

    /**
     * Constructor con todas las propiedades
     * @param id unico de Task
     * @param title titulo del la tarea
     * @param description decripcion de la tarea
     * @param done estado (hecho = true /no hecho = false)
     */
    public Task(Long id, String title, String description, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
    }

    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean getDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
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
