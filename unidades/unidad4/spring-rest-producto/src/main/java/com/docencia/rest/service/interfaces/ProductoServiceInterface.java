package com.docencia.rest.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.docencia.rest.domain.Producto;


public interface ProductoServiceInterface {
    
    Optional<Producto> find(Producto producto);
    Optional<Producto> findById(int id);

    List<Producto> findAll();

    Producto save(Producto producto);

    boolean delete(Producto producto);
    boolean deleteById(int id);

}
