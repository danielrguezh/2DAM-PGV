package com.docencia.restejercicio.controller;

import com.docencia.restejercicio.exception.ResourceNotFoundException;
import com.docencia.restejercicio.model.Task;
import com.docencia.restejercicio.model.User;
import com.docencia.restejercicio.service.TaskService;
import com.docencia.restejercicio.service.UserService;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API de gestion de usuarios")
public class UserController {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtener un usuario por su id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getProductoById(@PathVariable(value = "id") Long taskId)
            throws ResourceNotFoundException {
        User task = userService.getById(taskId);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(task);
    }

    

    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        final User updatedUser = userService.update(userId, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete/user/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        userService.delete(userId); 
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    @Operation(summary = "Listar todos los usuarios")
    @GetMapping("/")
    public List<User> getAllProductos() {
        return userService.getAll();
    }


    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/add/")
    public User createUser(@Valid @RequestBody User producto) {
        return userService.create(producto);
    }
}
