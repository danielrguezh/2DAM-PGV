package org.formacion.procesos.controller;

import org.formacion.procesos.controller.abstractas.ComandoControllerAbstract;
import org.formacion.procesos.domain.ProcessType;
import org.springframework.stereotype.Component;

@Component
public class ComandoControllerLS extends ComandoControllerAbstract {
    public ComandoControllerLS(){
        this.setTipo(ProcessType.LS);
    }

    @Override
    public void imprimeMensaje() {
        System.out.println("Estoy llamando a ComandoCtrollerLS.java");
    }
    
}