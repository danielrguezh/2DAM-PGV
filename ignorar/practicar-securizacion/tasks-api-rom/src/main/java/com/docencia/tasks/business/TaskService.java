package com.docencia.tasks.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.docencia.tasks.adapters.out.persistence.interfaces.ITaskPersistenceAdapter;
import com.docencia.tasks.business.interfaces.ITaskService;
import com.docencia.tasks.domain.model.Task;

/**
 * Clase del servicio de task
 * @author prorix
 * @version 1.0.0
 */
@Service
public class TaskService implements ITaskService {

    private final ITaskPersistenceAdapter repo;

    public TaskService(ITaskPersistenceAdapter repo) {
        this.repo = repo;
    }

    @Override
    public Task create(Task task) {
        task.setId(null);
        return repo.save(task);
    }

    @Override
    public List<Task> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Task> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Task> update(Long id, Task task) {
        return repo.findById(id).map(existing -> {
            if (task.getTitle() != null) {
                existing.setTitle(task.getTitle());
            }
            if (task.getDescription() != null) {
                existing.setDescription(task.getDescription());
            }
            existing.setCompleted(task.isCompleted());
            return repo.save(existing);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }
}
