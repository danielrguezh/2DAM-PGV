package com.docencia.tasks.adapters.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.domain.model.User;

/**ç
 * Mapper de user
 * @author prorix
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
  // API <-> Domain
  User toDomain(UserRequest request);

  UserResponse toResponse(User user);

  // Domain <-> JPA
  UserJpaEntity toJpa(User user);

  User toDomain(UserJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(UserRequest request, @MappingTarget User user);
}
