package com.docencia.tasks.adapters.in.api;

/**
 * @author prorix
 * @version 1.0.0
 */
public class TaskRequest {
  private String title;
  private String description;
  private Boolean completed;

  /**
   * Constructor vacio
   */
  public TaskRequest() {
  }

  /**
   * Constructor con todos los parametros
   * 
   * @param title       titulo del la tarea
   * @param description descripcion de la tarea
   * @param completed   si la tarea esta completa o no
   */
  public TaskRequest(String title, String description, Boolean completed) {
    this.title = title;
    this.description = description;
    this.completed = completed;
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

  public Boolean isCompleted() {
    return this.completed;
  }

  public Boolean getCompleted() {
    return this.completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }
}
