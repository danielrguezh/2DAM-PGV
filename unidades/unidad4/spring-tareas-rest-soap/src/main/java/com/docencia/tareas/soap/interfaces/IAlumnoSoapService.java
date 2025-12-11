package com.docencia.tareas.soap.interfaces;

import java.util.List;

import com.docencia.tareas.model.Alumno;

import jakarta.jws.*;

@WebService(
    targetNamespace = "http://ies.puerto.es/",
    name = "AlumnoPortType"
)
public interface IAlumnoSoapService {

    @WebMethod(operationName = "listar")
    List<Alumno> listar();
}
