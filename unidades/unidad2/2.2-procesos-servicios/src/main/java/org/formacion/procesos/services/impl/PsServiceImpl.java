package org.formacion.procesos.services.impl;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

/**
 * @author danielrguezh
 * @version 1.0.0
 */

@Component
public class PsServiceImpl extends ComandoServiceAbstract {
    /**
     * Constructor por defecto
     */
    public PsServiceImpl() {
        this.setTipo(Job.PS);
        this.setValidacion("^(\s*|(-?(xa|a|x|aux)))$");
    }
}
