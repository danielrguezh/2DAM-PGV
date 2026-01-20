package com.docencia.tasks.adapters.mapper;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-20T18:20:45+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toDomain(TaskRequest request) {
        if ( request == null ) {
            return null;
        }

        Task task = new Task();

        task.setTitle( request.getTitle() );
        task.setDescription( request.getDescription() );
        if ( request.getCompleted() != null ) {
            task.setCompleted( request.getCompleted() );
        }

        return task;
    }

    @Override
    public TaskResponse toResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setId( task.getId() );
        taskResponse.setTitle( task.getTitle() );
        taskResponse.setDescription( task.getDescription() );
        taskResponse.setCompleted( task.isCompleted() );

        return taskResponse;
    }

    @Override
    public TaskJpaEntity toJpa(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskJpaEntity taskJpaEntity = new TaskJpaEntity();

        taskJpaEntity.setId( task.getId() );
        taskJpaEntity.setTitle( task.getTitle() );
        taskJpaEntity.setDescription( task.getDescription() );
        taskJpaEntity.setCompleted( task.isCompleted() );

        return taskJpaEntity;
    }

    @Override
    public Task toDomain(TaskJpaEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Task task = new Task();

        task.setId( entity.getId() );
        task.setTitle( entity.getTitle() );
        task.setDescription( entity.getDescription() );
        task.setCompleted( entity.isCompleted() );

        return task;
    }

    @Override
    public void updateDomainFromRequest(TaskRequest request, Task task) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitle() != null ) {
            task.setTitle( request.getTitle() );
        }
        if ( request.getDescription() != null ) {
            task.setDescription( request.getDescription() );
        }
        if ( request.getCompleted() != null ) {
            task.setCompleted( request.getCompleted() );
        }
    }
}
