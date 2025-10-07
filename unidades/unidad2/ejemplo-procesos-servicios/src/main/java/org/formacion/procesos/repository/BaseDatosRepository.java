package org.formacion.procesos.repository;

import org.springframework.stereotype.Repository;

@Repository("BaseDatosRepository")
public class BaseDatosRepository implements IFicheroRepository{

    @Override
    public String saludar() {
        return "Te estoy saludando desde el repositorio de base de datos";
    }

}
