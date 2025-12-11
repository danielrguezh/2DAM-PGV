package com.docencia.tareas.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.docencia.tareas.soap.AlumnoSoapService;
import com.docencia.tareas.soap.TareaSoapService;

import jakarta.xml.ws.Endpoint;

@Configuration
public class CxfConfig {

    private final Bus bus;
    private final TareaSoapService tareaSoapService;
    private final AlumnoSoapService alumnoSoapService;


    public CxfConfig(Bus bus, TareaSoapService tareaSoapService, AlumnoSoapService alumnoSoapService) {
        this.bus = bus;
        this.tareaSoapService = tareaSoapService;
        this.alumnoSoapService = alumnoSoapService;
    }

    @Bean
    public Endpoint tareaEndpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus,tareaSoapService);
        endpoint.publish("/tareas");
        return endpoint;
    }

    @Bean
    public Endpoint alumnoEndpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus,alumnoSoapService);
        endpoint.publish("/alumnos");
        return endpoint;
    }
}
