package com.docencia.tasks.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.User;

/**
 * @author nexphernandez
 * @version 1.0.0
 */
public interface IUserPersistenceAdapter {

    /**
     * Funcion para buscar todos los usuarios
     *
     * @return lista de usuarios
     */
    List<User> getAll();

    /**
     * Funcion que te busca un usuario por el identificador
     *
     * @param id identificador del usuario
     * @return usuario buscado o null
     */
    Optional<User> getById(Long id);

    /**
     * Funcion para borrar un usuario por el id
     *
     * @param id identificador del usuario a borrar
     */
    boolean deleteById(Long id);

    /**
     * Funcion para guardar un usuario
     *
     * @param user usuario a guardar
     * @return usuario guardado
     */
    User save(User user, String rol);

    /**
     * Funcion para actualizar un usuario
     *
     * @param user usuario a guardar
     * @return usuario guardado
     */
    User update(User user, String rol);
}
