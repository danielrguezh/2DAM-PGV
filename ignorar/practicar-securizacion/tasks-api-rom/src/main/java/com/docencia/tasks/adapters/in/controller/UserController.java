package com.docencia.tasks.adapters.in.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.business.interfaces.IUserService;
import com.docencia.tasks.domain.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador de user
 * @author prorix
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API")
@CrossOrigin
public class UserController {

    private final IUserService service;
    private final UserMapper mapper;

    public UserController(UserMapper mapper, IUserService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request, @RequestParam String rol) {
        User created = service.create(mapper.toDomain(request), rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user (partial)")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest request,
            @RequestParam String rol) {

        User patch = new User();
        patch.setUserName(request.getUserName());
        patch.setPassword(request.getPassword());

        return service.update(id, patch, rol)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
