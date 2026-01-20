package com.docencia.aed.service;

import com.docencia.aed.entity.Book;

import java.util.List;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface IBookService {
    /**
     * Metodo que busca todas los libros de un autor
     * @param authorId
     * @return lista de libros
     */
    List<Book> findAll(Long authorId);
    /**
     * Metodo que busca un libro por su id
     * @param id del libro
     * @return libro
     */
    Book findById(Long id);
    /**
     * Metodo que crea un libro
     * @param authorId
     * @param book
     * @return libro creado
     */
    Book create(Long authorId, Book book);
}
