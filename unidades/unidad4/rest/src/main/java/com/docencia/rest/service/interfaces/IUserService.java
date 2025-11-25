package com.docencia.rest.service.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.docencia.rest.exception.ResourceNotFoundException;
import com.docencia.rest.model.User;

import jakarta.validation.Valid;

public interface IUserService {
List getAllUsers();
User getUserById(@PathVariable(value = "id") int userId) throws ResourceNotFoundException;
User createUser(@Valid @RequestBody User user);
User updateUser(@PathVariable(value = "id") int userId, @Valid @RequestBody User userDetails) throws ResourceNotFoundException;
boolean deleteUser(@PathVariable(value = "id") int userId) throws ResourceNotFoundException;
}