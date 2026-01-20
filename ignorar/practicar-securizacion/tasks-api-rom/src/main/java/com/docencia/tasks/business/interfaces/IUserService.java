package com.docencia.tasks.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.User;

/**
 * Interfaz del servicio de user
 * @author prorix
 * @version 1.0.0
 */
public interface IUserService {
  /**
   * Metodo que guarda un usuario
   * 
   * @param user usuario a guardar
   * @param rol  rol a asignar
   * @return usuario guardado(O null)
   */
  User create(User user, String rol);

  /**
   * Metodo que devuelve todos los usuarios
   * 
   * @return lista de usuarios
   */
  List<User> getAll();

  /**
   * Metodo que devuelve un usuario por el identificador
   * 
   * @param id identificador del usuario
   * @return usuario guardado(O null)
   */
  Optional<User> getById(Long id);

  /**
   * Metodo que actualiza un usuario
   * 
   * @param id    identificador del usuario
   * @param patch infomacion a guardar
   * @param rol   rol a asignar
   * @return usuario guardado(O null)
   */
  Optional<User> update(Long id, User patch, String Rol);

  /**
   * Metodo que borra un usuario por el identificador
   * 
   * @param id identificador del usuario
   * @return true/false
   */
  boolean delete(Long id);
}
