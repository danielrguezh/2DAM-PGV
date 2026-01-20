package com.docencia.tasks.adapters.mapper;

import org.mapstruct.*;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.out.persistence.jpa.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;

/**
 * Mapper de task
 * @author prorix
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

  Task toDomain(TaskRequest request);

  TaskResponse toResponse(Task task);

  TaskJpaEntity toJpa(Task task);

  Task toDomain(TaskJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(TaskRequest request, @MappingTarget Task task);
}
