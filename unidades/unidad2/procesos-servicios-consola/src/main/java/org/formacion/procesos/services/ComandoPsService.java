package org.formacion.procesos.services;

import org.formacion.procesos.domain.ProcessType;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

@Component
public class ComandoPsService extends ComandoServiceAbstract {
    public ComandoPsService(){
        this.setTipo(ProcessType.PS);
    }

    @Override
    public void imprimeMensaje() {
        System.out.println("Estoy llamando a ComandoCtrollerPS.java");
    }
    
    @Override
    public boolean validar(String[] arrayComando) {
        if (!super.validarComando()){
            return false;
        }
        return true;
    }
}