package com.docencia.restejercicio.repository;

import com.docencia.restejercicio.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author danielrguezh 
 * @version 1.0.0
 */
@Repository
public class UserRepository {

    List<User> users= new ArrayList<>();;

    /**
     * Metodo que encuentra todas los users
     * @return lsita de users
     */
    public List<User> findAll() {
        return users;
    }

    /**
     * Metodo que retorna un user segun su id
     * @param id unica del user
     * @return user si se encuentra
     */
    public Optional<User> findById(Long id) {
        User user = new User(id);
        int posicion =users.indexOf(user);
        if (posicion<0) {
            return null;
        }
       return Optional.ofNullable(users.get(posicion));
    }

    /**
     * Metodo que guarda un user en la lista
     * @param user a guardar
     * @return user
     */
    public User save(User user) {
        if (user == null) {
            return null;
        }

        if (existsById(user.getId())) {
            
            users.add(users.indexOf(user),user);
        }

        if (user.getId()==null){
            user.setId((long) (users.size()+1));
            users.add(user);
        }
        
        return user;
    }

    /**
     * Metodo que elimina un user segun su id
     * @param id unica del user
     */
    public void deleteById(Long id) {
        User user = new User(id);
        users.remove(user);
    }

    /**
     * Metodo que confirma si un user se encuentra en la lista
     * @param id unica del user
     * @return true/false
     */
    public boolean existsById(Long id) {
        User user = new User(id);
        return users.contains(user);
    }
}
