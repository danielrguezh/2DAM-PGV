package com.docencia.tasks.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.User;

/**
 * @author prorix
 * @version 1.0.0
 */
public interface IUserPersistenceAdapter {

    /**
     * Metodo que busca todos los usuarios
     *
     * @return lista de usuarios
     */
    List<User> getAll();

    /**
     * Metodo que busca un usuario por el id
     *
     * @param id id del usuario
     * @return usuario buscado o null
     */
    Optional<User> getById(Long id);

    /**
     * Metodo que borra un usuario por el id
     *
     * @param id id del usuario a borrar
     */
    boolean deleteById(Long id);

    /**
     * Metodo que guarda un usuario
     *
     * @param user usuario a guardar
     * @return usuario guardado
     */
    User save(User user, String rol);

    /**
     * Metodo que actualiza un usuario
     *
     * @param user usuario a guardar
     * @return usuario guardado
     */
    User update(User user, String rol);
}
