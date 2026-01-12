package com.docencia.tasks.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.business.TaskService;
import com.docencia.tasks.domain.model.Task;
import com.docencia.tasks.entitys.TaskEntity;
import com.docencia.tasks.mapper.TaskMapper;
import com.docencia.tasks.repository.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks API")
@CrossOrigin
public class TaskController {

    private final TaskService service;
    private TaskMapper mapper;

    @Autowired
    public void setMapper(TaskMapper mapper) {
        this.mapper = mapper;
    }

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public List<TaskEntity> getAll() {
        return null;
    }

    @PostMapping
    @Operation(summary = "Create new task")
    public TaskResponse create(@NotNull @RequestBody TaskRequest taskRequest) {
        Task task = mapper.requestToTask(taskRequest);
        task=service.createTask(task);
        return mapper.taskToResponse(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    public TaskEntity update(@PathVariable Long id, @RequestBody TaskEntity task) {
        // task.setCompleted(task.isCompleted());
        // task.setTitle(task.getTitle());
        // task.setDescription(task.getDescription());
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public void delete(@PathVariable Long id) {
        // repo.deleteById(id);
    }

    
}
