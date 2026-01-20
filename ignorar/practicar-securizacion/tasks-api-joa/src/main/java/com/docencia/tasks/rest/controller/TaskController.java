package com.docencia.tasks.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import com.docencia.tasks.entitys.TaskEntity;
import com.docencia.tasks.repository.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks API")
@CrossOrigin
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public List<TaskEntity> getAll() {
        return repo.findAll();
    }

    @PostMapping
    @Operation(summary = "Create new task")
    public TaskEntity create(@RequestBody TaskEntity task) {
        return repo.save(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    public TaskEntity update(@PathVariable Long id, @RequestBody TaskEntity task) {
        task.setCompleted(task.isCompleted());
        task.setTitle(task.getTitle());
        task.setDescription(task.getDescription());
        return repo.save(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
