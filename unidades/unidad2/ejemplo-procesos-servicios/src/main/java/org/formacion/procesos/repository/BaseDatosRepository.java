package org.formacion.procesos.repository;

import org.formacion.procesos.repository.interfaces.IAlmacenamientoRepository;
import org.springframework.stereotype.Repository;

@Repository("BaseDatosRepository")
public class BaseDatosRepository implements IAlmacenamientoRepository{

    @Override
    public String saludar() {
        return "Te estoy saludando desde el repositorio de base de datos";
    }

}
