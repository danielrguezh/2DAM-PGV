package org.formacion.procesos.services;

import org.formacion.procesos.domain.ProcessType;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

@Component
public class ComandoLsService extends ComandoServiceAbstract{

    public ComandoLsService() {
        this.setTipo(ProcessType.LS);
        this.setValidacion("^(\s*|(-(la|l|a)))$");
    }
}
