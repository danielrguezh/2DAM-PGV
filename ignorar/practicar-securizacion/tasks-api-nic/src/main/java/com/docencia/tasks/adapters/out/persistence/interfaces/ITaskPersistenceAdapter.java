package com.docencia.tasks.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;

/**
 * @author nexphernandez
 * @version 1.0.0
 */
public interface ITaskPersistenceAdapter {

    /**
     * Funcion para guardar una tarea
     *
     * @param task tarea a guardar
     * @return tarea guardada
     */
    Task save(Task task);

    /**
     * Funcion para buscar todas las tareas
     *
     * @return lista de tareas
     */
    List<Task> findAll();

    /**
     * Funcion que te busca una tarea por el identificador
     *
     * @param id identificador de la tarea
     * @return la tarea buscada o null
     */
    Optional<Task> findById(Long id);

    /**
     * Funcion para borrar una tarea por el id
     *
     * @param id identificador de la tarea a borrar
     */
    void deleteById(Long id);

    /**
     * Funcion que varifica si existe una tarea
     *
     * @param id identificador de la tarea verificar
     * @return true/false
     */
    boolean existsById(Long id);
}
