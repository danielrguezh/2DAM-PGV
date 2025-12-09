package com.docencia.restejercicio.controller;

import com.docencia.restejercicio.common.ApiRestController;
import com.docencia.restejercicio.exception.ResourceNotFoundException;
import com.docencia.restejercicio.model.Task;
import com.docencia.restejercicio.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author danielrguezh 
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task", description = "API de gestion de tareas")
public class TaskController {
    TaskService taskService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Obtener una tarea por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getProductoById(@PathVariable(value = "id") Long taskId)
            throws ResourceNotFoundException {
        Task task = taskService.getById(taskId);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(task);
    }

    @Operation(summary = "Actualizar una tarea existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/update/user/{id}")
    public ResponseEntity<Task> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody Task userDetails) throws ResourceNotFoundException {
        final Task updatedUser = taskService.update(userId, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Eliminar una tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/delete/user/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        taskService.delete(userId); 
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    @Operation(summary = "Listar todas las tareas")
    @GetMapping("/")
    public List<Task> getAllProductos() {
        return taskService.getAll();
    }


    @Operation(summary = "Crear una nueva tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/add/")
    public Task createUser(@Valid @RequestBody Task producto) {
        return taskService.create(producto);
    }

}
