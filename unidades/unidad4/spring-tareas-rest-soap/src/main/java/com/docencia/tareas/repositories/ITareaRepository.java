package com.docencia.tareas.repositories;

import java.util.List;

import com.docencia.tareas.model.Tarea;

public interface ITareaRepository {
    /**
     * 
     * @param tarea
     * @return
     */
    public Tarea add(Tarea tarea);

    /**
     * 
     * @param tarea
     * @return
     */
    public boolean delete(Tarea tarea);

    /**
     * 
     * @param tarea
     * @return
     */
    public Tarea findBy(Tarea tarea);

    /**
     * 
     * @param tarea
     * @return
     */
    public Tarea update(Tarea tarea);

    /**
     * 
     * @return
     */
    public List<Tarea> all();
}
