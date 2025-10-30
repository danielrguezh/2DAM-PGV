package org.formacion.procesos.services.impl;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

/**
 * @author danielrguezh
 * @version 1.0.0
 */

@Component
public class LsofServiceImpl extends ComandoServiceAbstract{

    /**
     * Constructor por defecto
     */
    public LsofServiceImpl() {
        this.setTipo(Job.LSOF);
        this.setValidacion("^(\\s*|(-(i)))$");
    }
}