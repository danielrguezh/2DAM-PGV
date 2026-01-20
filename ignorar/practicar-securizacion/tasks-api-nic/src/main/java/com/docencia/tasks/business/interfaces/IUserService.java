package com.docencia.tasks.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.User;
/**
 * @author nexphernandez
 * @version 1.0.0 
 * Interfaz del servicio de user
 */
public interface IUserService {
  /**
   * Funcion para guardar un usuario
   * @param user usuario a guardar
   * @param rol rol a asignar
   * @return usuario guardado/null
   */
  User create(User user,String rol);
  /**
   * Funcion para obtener todos los usuarios
   * @return lista de usuarios
   */
  List<User> getAll();
  /**
   * Funcion para obtener un usuario por el identificador
   * @param id identificador del usuario
   * @return usuario guardado/null
   */
  Optional<User> getById(Long id);
  /**
   * Funcion para actualizar un usuario
   * @param id identificador del usuario
   * @param patch infomacion a guardar
   * @param rol rol a asignar
   * @return usuario guardado/null
   */
  Optional<User> update(Long id, User patch,String Rol);

  /**
   * Funcion que borra un usuario por el identificador
   * @param id identificador del usuario
   * @return true/false
   */
  boolean delete(Long id);
}
