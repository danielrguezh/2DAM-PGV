package com.docencia.tasks.adapters.mapper;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.domain.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-20T18:59:02+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDomain(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setUserName( request.getUserName() );
        user.setPassword( request.getPassword() );

        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setUserName( user.getUserName() );
        userResponse.setPassword( user.getPassword() );
        userResponse.setId( user.getId() );

        return userResponse;
    }

    @Override
    public UserJpaEntity toJpa(User user) {
        if ( user == null ) {
            return null;
        }

        UserJpaEntity userJpaEntity = new UserJpaEntity();

        userJpaEntity.setId( user.getId() );
        userJpaEntity.setUserName( user.getUserName() );
        userJpaEntity.setPassword( user.getPassword() );

        return userJpaEntity;
    }

    @Override
    public User toDomain(UserJpaEntity entity) {
        if ( entity == null ) {
            return null;
        }

        User user = new User();

        user.setUserName( entity.getUserName() );
        user.setPassword( entity.getPassword() );
        user.setId( entity.getId() );

        return user;
    }

    @Override
    public void updateDomainFromRequest(UserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( request.getUserName() != null ) {
            user.setUserName( request.getUserName() );
        }
        if ( request.getPassword() != null ) {
            user.setPassword( request.getPassword() );
        }
    }
}
