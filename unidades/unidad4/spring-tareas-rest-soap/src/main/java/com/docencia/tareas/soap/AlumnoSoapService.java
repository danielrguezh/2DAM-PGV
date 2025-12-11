package com.docencia.tareas.soap;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docencia.tareas.model.Alumno;
import com.docencia.tareas.service.IAlumnoService;
import com.docencia.tareas.soap.interfaces.IAlumnoSoapService;

import jakarta.jws.*;

@WebService(
    serviceName = "AlumnoService",
    portName = "AlumnoPort",
    targetNamespace = "http://alumno.ies.puerto.es/",
    endpointInterface = "com.docencia.tareas.soap.interfaces.IAlumnoSoapService"
)
@Service
public class AlumnoSoapService implements IAlumnoSoapService{
    private final IAlumnoService alumnoService;


    public AlumnoSoapService(IAlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }


    @Override
    public List<Alumno> listar() {
        return alumnoService.listarTodas();
    }

}
