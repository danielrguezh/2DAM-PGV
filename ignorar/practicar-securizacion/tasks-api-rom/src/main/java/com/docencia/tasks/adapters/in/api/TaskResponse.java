package com.docencia.tasks.adapters.in.api;

import java.util.Objects;

/**
 * @author prorix
 * @version 1.0.0
 */
public class TaskResponse {
  private Long id;
  private String title;
  private String description;
  private boolean completed;

  /**
   * Constructor vacio
   */
  public TaskResponse() {
  }

  /**
   * Consturctor con solo la clave primaria
   * 
   * @param id identificador del la tarea
   */
  public TaskResponse(Long id) {
    this.id = id;
  }

  /**
   * Constructor con todos los parametros
   * 
   * @param id          identificador de la tarea
   * @param title       titulo del la tarea
   * @param description descripcion de la tarea
   * @param completed   si la tarea esta completa o no
   */
  public TaskResponse(Long id, String title, String description, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
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

  public boolean isCompleted() {
    return this.completed;
  }

  public boolean getCompleted() {
    return this.completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof TaskResponse)) {
      return false;
    }
    TaskResponse taskResponse = (TaskResponse) o;
    return Objects.equals(id, taskResponse.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
