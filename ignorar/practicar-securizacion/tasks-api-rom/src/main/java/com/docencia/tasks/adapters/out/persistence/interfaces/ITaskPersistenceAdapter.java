package com.docencia.tasks.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;

/**
 * @author prorix
 * @version 1.0.0
 */
public interface ITaskPersistenceAdapter {

    /**
     * Metodo que guarda una tarea
     *
     * @param task tarea a guardar
     * @return tarea guardada
     */
    Task save(Task task);

    /**
     * Metodo que busca todas las tareas
     *
     * @return lista de tareas
     */
    List<Task> findAll();

    /**
     * Metodo que busca una tarea por el id
     *
     * @param id id de la tarea
     * @return la tarea buscada o null
     */
    Optional<Task> findById(Long id);

    /**
     * Metodo que borra una tarea por el id
     *
     * @param id id de la tarea a borrar
     */
    void deleteById(Long id);

    /**
     * Metodo que verifica si existe una tarea
     *
     * @param id id de la tarea verificar
     * @return true/false
     */
    boolean existsById(Long id);
}
