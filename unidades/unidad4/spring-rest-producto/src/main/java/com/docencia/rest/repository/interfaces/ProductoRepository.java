package com.docencia.rest.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.docencia.rest.modelo.ProductoEntity;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer>{

}
