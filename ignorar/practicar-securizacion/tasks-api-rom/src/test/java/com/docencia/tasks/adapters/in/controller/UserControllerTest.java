package com.docencia.tasks.adapters.in.controller;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.business.interfaces.IUserService;
import com.docencia.tasks.domain.model.User;

class UserControllerTest {

    IUserService service;
    UserMapper mapper;
    UserController controller;

    @BeforeEach
    void setUp() {
        service = mock(IUserService.class);
        mapper = mock(UserMapper.class);
        controller = new UserController(mapper, service);
    }

    @Test
    void getAll_shouldReturnMappedList() {
        User user = new User(1L, "usuario", "pass", null);
        UserResponse resp = new UserResponse(1L, "usuario", "pass");

        when(service.getAll()).thenReturn(List.of(user));
        when(mapper.toResponse(user)).thenReturn(resp);

        List<UserResponse> result = controller.getAll();

        assertEquals(1, result.size());
        assertEquals(resp, result.get(0));
        verify(service).getAll();
        verify(mapper).toResponse(user);
    }

    @Test
    void getById_existingUser_shouldReturnOk() {
        User user = new User(1L, "usuario", "pass", null);
        UserResponse resp = new UserResponse(1L, "usuario", "pass");

        when(service.getById(1L)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(resp);

        ResponseEntity<UserResponse> response = controller.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
        verify(service).getById(1L);
        verify(mapper).toResponse(user);
    }

    @Test
    void getById_nonExistingUser_shouldReturnNotFound() {
        when(service.getById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = controller.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).getById(1L);
    }

    @Test
    void create_shouldReturnCreated() {
        UserRequest request = new UserRequest();
        request.setUserName("usuario");
        request.setPassword("pass");

        User user = new User(null, "usuario", "pass", null);
        User created = new User(1L, "usuario", "pass", null);
        UserResponse resp = new UserResponse(1L, "usuario", "pass");

        when(mapper.toDomain(request)).thenReturn(user);
        when(service.create(any(User.class), eq("USER"))).thenReturn(created);
        when(mapper.toResponse(created)).thenReturn(resp);

        ResponseEntity<UserResponse> response = controller.create(request, "USER");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resp, response.getBody());
        verify(service).create(any(User.class), eq("USER"));
        verify(mapper).toResponse(created);
    }

    @Test
    void update_existingUser_shouldReturnOk() {
        UserRequest request = new UserRequest();
        request.setUserName("newName");
        request.setPassword("newPass");

        User patched = new User(null, "newName", "newPass", null);
        User updated = new User(1L, "newName", "newPass", null);
        UserResponse resp = new UserResponse(1L, "newName", "newPass");

        when(service.update(eq(1L), any(User.class), eq("USER"))).thenReturn(Optional.of(updated));
        when(mapper.toResponse(updated)).thenReturn(resp);

        ResponseEntity<UserResponse> response = controller.update(1L, request, "USER");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
        verify(service).update(eq(1L), any(User.class), eq("USER"));
        verify(mapper).toResponse(updated);
    }

    @Test
    void update_nonExistingUser_shouldReturnNotFound() {
        UserRequest request = new UserRequest();
        request.setUserName("newName");
        request.setPassword("newPass");

        when(service.update(eq(1L), any(User.class), eq("USER"))).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = controller.update(1L, request, "USER");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).update(eq(1L), any(User.class), eq("USER"));
    }

    @Test
    void delete_existingUser_shouldReturnNoContent() {
        when(service.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete(1L);
    }

    @Test
    void delete_nonExistingUser_shouldReturnNotFound() {
        when(service.delete(1L)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service).delete(1L);
    }
}