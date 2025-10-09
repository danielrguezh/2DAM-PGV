package org.formacion.procesos.service;


import org.formacion.procesos.component.interfaces.IFicheroComponent;
import org.formacion.procesos.service.interfaces.IProcesos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Procesos implements IProcesos {
    
    IFicheroComponent componenteFichero;

    @Autowired
    public void setComponenteFichero(IFicheroComponent componenteFichero) {
        this.componenteFichero = componenteFichero;
    }
    
    @Override
    public void ejecutar() {
        System.out.println("Ejecutando lógica del proceso...");
        System.out.println(componenteFichero.mensajse());
    }
    
}