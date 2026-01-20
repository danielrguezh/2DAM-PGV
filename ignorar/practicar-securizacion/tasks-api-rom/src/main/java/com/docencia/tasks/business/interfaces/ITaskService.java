package com.docencia.tasks.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;

/**
 * Interfaz del servicio de task
 * @author prorix
 * @version 1.0.0
 */
public interface ITaskService {
  /**
   * Metodo que guarda una tarea
   * 
   * @param task tarea a guardar
   * @return tarea guardada(O null)
   */
  Task create(Task task);

  /**
   * Metodo que devuelve todas las tareas
   * 
   * @return lista de tareas
   */
  List<Task> getAll();

  /**
   * Metodo que devuelve una tarea por el identificador
   * 
   * @param id identificador de la tarea
   * @return tarea buscada(O null)
   */
  Optional<Task> getById(Long id);

  /**
   * Metodo que actualiza una tarea
   * 
   * @param id    identificador de la tarea
   * @param patch infomacion a guardar
   * @return tarea guardada(O null)
   */
  Optional<Task> update(Long id, Task patch);

  /**
   * Metodo que borra una tarea por el identificador
   * 
   * @param id identificador de la tarea
   * @return true/false
   */
  boolean delete(Long id);
}
