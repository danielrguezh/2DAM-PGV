package com.docencia.tasks.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;
/**
 * @author nexphernandez
 * @version 1.0.0 
 * Interfaz del servicio de task
 */
public interface ITaskService {
  /**
   * Funcion para guardar una tarea
   * @param task tarea a guardar
   * @return tarea guardada/null
   */
  Task create(Task task);
  /**
   * Funcion para obtener todas las tareas
   * @return lista de tareas
   */
  List<Task> getAll();
  /**
   * Funcion para obtener una tarea por el identificador
   * @param id identificador de la tarea
   * @return tarea buscada/null
   */
  Optional<Task> getById(Long id);
  /**
   * Funcion para actualizar una tarea
   * @param id identificador de la tarea
   * @param patch infomacion a guardar
   * @return tarea guardada/null
   */
  Optional<Task> update(Long id, Task patch);

  /**
   * Funcion que borra una tarea por el identificador
   * @param id identificador de la tarea
   * @return true/false
   */
  boolean delete(Long id);
}
