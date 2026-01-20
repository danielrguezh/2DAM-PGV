package com.docencia.aed.repository;

import com.docencia.aed.entity.Author;

import java.util.List;
import java.util.Optional;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface AuthorRepository {
    /**
     * Metodo que retorna todos los autores
     * @return lista de autores
     */
    List<Author> findAll();
    /**
     * Metodo que busca un autor por su id
     * @param id del autor
     * @return autor
     */
    Optional<Author> findById(Long id);
    /**
     * Metodo que verifica un autor por su id
     * @param id del autor
     * @return true/false
     */
    boolean existsById(Long id);
    /**
     *  Metodo que crea un autor
     * @param author a crear
     * @return autor creado
     */
    Author save(Author author);
}
