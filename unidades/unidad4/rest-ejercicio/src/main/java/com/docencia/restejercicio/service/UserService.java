package com.docencia.restejercicio.service;

import com.docencia.restejercicio.model.User;
import com.docencia.restejercicio.repository.UserRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danielrguezh 
 * @version 1.0.0
 */
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService (UserRepository repository) {
        this.repository = repository;
    }

    

    /**
     * Metodo que obtiene todos los users
     * @return lista de users
     */
    public List<User> getAll() {
        return repository.findAll();
    }

    /**
     * Metodo que retorna un user segun su id
     * @param id unica del user
     * @return user
     */
    public User getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Metodo que crea un user en la lista
     * @param user a crear
     * @return user
     */
    public User create(User user) {
        return repository.save(user);
    }

    /**
     * Metodo que actualiza un user en la lista
     * @param id del user
     * @param update datos a actualizar del user
     * @return user
     */
    public User update(Long id, User update) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        user.setUsername(update.getUsername());
        user.setEmail(update.getEmail());
        return repository.save(user);
    }
    
    /**
     * Metodo que elimina un user segun su id
     * @param id unica del user
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
