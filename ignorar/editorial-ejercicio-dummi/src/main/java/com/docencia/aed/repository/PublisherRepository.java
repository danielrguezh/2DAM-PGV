package com.docencia.aed.repository;

import com.docencia.aed.entity.Publisher;

import java.util.List;

/**
 * Repositorio de editoriales (sin BBDD).
 */
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface PublisherRepository {
    /**
     * Metodo que busca todas las editoriales
     * @return lista de editoriales
     */
    List<Publisher> findAll();
    /**
     * Metodo que crea una editorial
     * @param publisher editorial a crear
     * @return editorial creada
     */
    Publisher save(Publisher publisher);
}
