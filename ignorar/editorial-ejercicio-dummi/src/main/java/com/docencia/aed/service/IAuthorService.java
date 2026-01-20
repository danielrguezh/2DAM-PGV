package com.docencia.aed.service;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;

import java.util.List;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface IAuthorService {
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
    Author findById(Long id);
    /**
     *  Metodo que crea un autor
     * @param author a crear
     * @return autor creado
     */
    Author create(Author author);
    /**
     * Metodo que busca los libros de un autor
     * @param authorId
     * @return lisata de libros
     */
    List<Book> findBooksByAuthor(Long authorId);
}
