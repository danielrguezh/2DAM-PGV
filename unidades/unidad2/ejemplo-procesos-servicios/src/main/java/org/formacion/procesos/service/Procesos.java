package org.formacion.procesos.service;


import org.formacion.procesos.component.IFicheroComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Procesos {
    @Autowired
    IFicheroComponent componenteFichero;

    public void ejecutar() {
        System.out.println("Ejecutando lógica del proceso...");
        System.out.println(componenteFichero.mensajse());
    }
    
}