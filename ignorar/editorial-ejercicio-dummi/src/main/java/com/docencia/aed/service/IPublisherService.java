package com.docencia.aed.service;

import com.docencia.aed.entity.Publisher;

import java.util.List;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface IPublisherService {
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
    Publisher create(Publisher publisher);
}
