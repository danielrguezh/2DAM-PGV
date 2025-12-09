package com.docencia.restejercicio.service;

import com.docencia.restejercicio.model.Task;
import com.docencia.restejercicio.repository.TaskRepository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danielrguezh 
 * @version 1.0.0
 */
@Service
public class TaskService {

    private  final TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Metodo que obtiene todas las tasks
     * @return lista de tasks
     */
    public List<Task> getAll() {
        return repository.findAll();
    }

    /**
     * Metodo que retorna un task segun su id
     * @param id unica del task
     * @return task
     */
    public Task getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Metodo que crea un task en la lista
     * @param task a crear
     * @return task
     */
    public Task create(Task task) {
        return repository.save(task);
    }

    /**
     * Metodo que actualiza un task en la lista
     * @param id del task
     * @param update datos a actualizar del task
     * @return task
     */
    public Task update(Long id, Task update) {
        Task task = getById(id);
        if (task == null) {
            return null;
        }
        task.setTitle(update.getTitle());
        task.setDescription(update.getDescription());
        task.setDone(update.getDone());
        return repository.save(task);
    }

    /**
     * Metodo que elimina un task segun su id
     * @param id unica del task
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
