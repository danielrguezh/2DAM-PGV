package com.docencia.rest.service.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.docencia.rest.exception.ResourceNotFoundException;
import com.docencia.rest.model.User;

public interface UserServiceInterface {
    public List<User> getAllUsers();
    public User getUserById(@PathVariable(value = "id") int userId) throws ResourceNotFoundException;
    public User createUser(@Valid @RequestBody User user);
}
