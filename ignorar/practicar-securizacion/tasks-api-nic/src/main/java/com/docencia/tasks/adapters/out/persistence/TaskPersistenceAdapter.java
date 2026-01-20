package com.docencia.tasks.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.adapters.out.persistence.interfaces.ITaskPersistenceAdapter;
import com.docencia.tasks.adapters.out.persistence.jpa.TaskJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.TaskRepositoryRepository;
import com.docencia.tasks.domain.model.Task;
/**
 * @author nexphernandez
 * @version 1.0.0 Clase persistence de task
 */
@Component
public class TaskPersistenceAdapter implements ITaskPersistenceAdapter {

  private final TaskRepositoryRepository jpaRepo;
  private final TaskMapper mapper;

  /**
   * Constructor para inicializar la clase
   * @param jpaRepo interfaz jpa de task
   * @param mapper mapers de task
   */
  public TaskPersistenceAdapter(TaskRepositoryRepository jpaRepo, TaskMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Task save(Task task) {
    TaskJpaEntity saved = jpaRepo.save(mapper.toJpa(task));
    return mapper.toDomain(saved);
  }

  @Override
  public List<Task> findAll() {
    return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Task> findById(Long id) {
    return jpaRepo.findById(id).map(mapper::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    jpaRepo.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepo.existsById(id);
  }
}
