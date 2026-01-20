package com.docencia.tasks.adapters.mapper;

import org.mapstruct.*;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  /**
   * Convierte de Request (DTO de entrada) a Dominio.
   * Se ignoran campos que no coincidan automaticamente o se necesite logica
   * custom.
   */
  Task toDomain(TaskRequest request);

  /**
   * Convierte de Dominio a Response (DTO de salida).
   * Util para exponer solo los datos necesarios en la API.
   */
  TaskResponse toResponse(Task task);

  // Domain <-> JPA

  /**
   * Convierte el modelo de Dominio a Entidad JPA.
   * Necesario para guardar en base de datos.
   */
  TaskJpaEntity toJpa(Task task);

  /**
   * Convierte de Entidad JPA a modelo de Dominio.
   * Se usa al leer de base de datos.
   */
  Task toDomain(TaskJpaEntity entity);

  /**
   * Actualiza un objeto de Dominio existente con datos del Request.
   * NullValuePropertyMappingStrategy.IGNORE evita sobreescribir con nulls.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(TaskRequest request, @MappingTarget Task task);
}
