package org.formacion.procesos.component;

import org.formacion.procesos.repository.IFicheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("component1")
public class FicheroComponent implements IFicheroComponent{
    @Autowired
    @Qualifier("BaseDatosRepository")
    IFicheroRepository baseDatosRepository;

    @Autowired
    @Qualifier("FicheroRepository")
    IFicheroRepository ficheroRepository;

    @Override
    public String mensajse() {
        return baseDatosRepository.saludar() + "\n" + ficheroRepository.saludar();
    }

}
