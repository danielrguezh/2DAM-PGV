package com.docencia.tasks.business;

import org.springframework.stereotype.Service;

import com.docencia.tasks.adapters.out.persistence.ITaskPersistenceAdapter;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

  private final ITaskPersistenceAdapter repo;

  public TaskService(ITaskPersistenceAdapter repo) {
    this.repo = repo;
  }

  /**
   * Crea una nueva tarea.
   * Se fuerza el ID a null para asegurar que se crea una nueva entidad y no se
   * actualiza una existente.
   */
  public Task create(Task task) {
    // Aseguramos alta nueva
    task.setId(null);
    return repo.save(task);
  }

  /**
   * Obtiene todas las tareas.
   * En un entorno real, esto deberia estar paginado.
   */
  public List<Task> getAll() {
    return repo.findAll();
  }

  /**
   * Busca una tarea por su ID.
   * Retorna un Optional para manejar el caso de "no encontrado" de forma segura.
   */
  public Optional<Task> getById(Long id) {
    return repo.findById(id);
  }

  /**
   * Actualiza parcialmente una tarea.
   * Solo actualiza los campos que vienen ne el patch (no nulos), excepto
   * 'completed' que es booleano.
   */
  public Optional<Task> update(Long id, Task patch) {
    return repo.findById(id).map(existing -> {
      if (patch.getTitle() != null)
        existing.setTitle(patch.getTitle());
      if (patch.getDescription() != null)
        existing.setDescription(patch.getDescription());
      // En esta implementacion, completed siempre se actualiza con el valor de patch
      existing.setCompleted(patch.isCompleted());
      return repo.save(existing);
    });
  }

  /**
   * Elimina una tarea por ID.
   * Retorna true si existia y se elimino, false si no existia.
   */
  public boolean delete(Long id) {
    if (!repo.existsById(id))
      return false;
    repo.deleteById(id);
    return true;
  }
}
