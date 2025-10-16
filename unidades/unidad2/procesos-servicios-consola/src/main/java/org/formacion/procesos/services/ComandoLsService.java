package org.formacion.procesos.services;

import org.formacion.procesos.domain.ProcessType;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

@Component
public class ComandoLsService extends ComandoServiceAbstract {
    public ComandoLsService(){
        this.setTipo(ProcessType.LS);
    }

    @Override
    public void imprimeMensaje() {
        System.out.println("Estoy llamando a ComandoCtrollerLS.java");
    }

    @Override
    public boolean validar(String[] arrayComando) {
        if (!super.validarComando()){
            return false;
        }
        String parametro= arrayComando[1];
        return true;
    }
    
}