package com.docencia.tasks.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.domain.model.Task;
import com.docencia.tasks.entitys.TaskEntity;

@Mapper(componentModel = "Spring")
public interface TaskMapper {
    @Mapping(source = "identificador", target = "id")
    @Mapping(source = "titulo", target = "title")
    @Mapping(source = "descripcion", target = "description")
    @Mapping(source = "completado", target = "completed")
    public Task requestToTask(TaskRequest source);

    public List<Task> toTaskList(List<TaskRequest> requests);
    
    public Task entityToTask(TaskEntity source);

    public List<Task> entitestoTaskList(List<TaskEntity> requests);

    public TaskResponse taskToResponse(Task task);
}
