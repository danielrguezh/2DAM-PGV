package com.docencia.restejercicio.repository;

import com.docencia.restejercicio.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author danielrguezh 
 * @version 1.0.0
 */
@Repository
public class TaskRepository {

    List<Task> tasks= new ArrayList<>();

    /**
     * Metodo que encuentra todas las tasks
     * @return lsita de tasks
     */
    public List<Task> findAll() {
        return tasks;
    }

    /**
     * Metodo que retorna una task segun su id
     * @param id unica de la task
     * @return task si se encuentra
     */
    public Optional<Task> findById(Long id) {
        Task task= new Task(id);
        int posicion =tasks.indexOf(task);
        if (posicion<0) {
            return null;
        }
       return Optional.ofNullable(tasks.get(posicion));
    }

    /**
     * Metodo que guarda una task en la lista
     * @param task a guardar
     * @return task
     */
    public Task save(Task task) {
        if (task == null ) {
            return null;
        }

        if (existsById(task.getId())) {
            
            tasks.add(tasks.indexOf(task),task);
        }
        
        if (task.getId()==null){
            task.setId((long) (tasks.size()+1));
            tasks.add(task);
        }
        
        return task;
    }

    /**
     * Metodo que elimina una task segun su id
     * @param id unica de la task
     */
    public void deleteById(Long id) {
        Task task= new Task(id);
        tasks.remove(task);
    }

    /**
     * Metodo que confirma si una task se encuentra en la lista
     * @param id unica de la task
     * @return true/false
     */
    public boolean existsById(Long id) {
        Task task= new Task(id);
        return tasks.contains(task);
    }
}
