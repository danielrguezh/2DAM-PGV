package com.docencia.tasks.entities;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * @author danielrguezh
 * @version 1.0.0
 */
@Entity
public class TaskEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    public TaskEntity() {}

    public TaskEntity(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCompleted(boolean completed) { this.completed = completed; }


    public TaskEntity(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TaskEntity)) {
            return false;
        }
        TaskEntity taskEntity = (TaskEntity) o;
        return Objects.equals(id, taskEntity.id) && Objects.equals(title, taskEntity.title) && Objects.equals(description, taskEntity.description) && completed == taskEntity.completed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, completed);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", completed='" + isCompleted() + "'" +
            "}";
    }
    
}
