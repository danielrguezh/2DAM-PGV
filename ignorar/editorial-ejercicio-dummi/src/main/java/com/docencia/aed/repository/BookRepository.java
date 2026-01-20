package com.docencia.aed.repository;

import com.docencia.aed.entity.Book;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de libros (sin BBDD).
 */
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface BookRepository {
    /**
     * Metodo que busca todas los libros
     * @return lista de libros
     */
    List<Book> findAll();
    /**
     * Metodo que busca un libro por su id
     * @param id del libro
     * @return libro
     */
    Optional<Book> findById(Long id);
    /**
     * Metodo que crea un libro
     * @param authorId
     * @param book
     * @return libro creado
     */
    Book save(Book book);
    /**
     * Metodo que busca los libros de un autor
     * @param authorId
     * @return lisata de libros
     */
    List<Book> findByAuthorId(Long authorId);
}
