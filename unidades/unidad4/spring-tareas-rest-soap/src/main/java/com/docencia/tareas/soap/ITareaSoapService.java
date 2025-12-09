package com.docencia.tareas.soap;

import java.util.List;

import com.docencia.tareas.model.Tarea;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(
    targetNamespace = "http://ies.puerto.es/ws/tarea",
    name = "TareaPortType"
)
public interface ITareaSoapService {

    @WebMethod(operationName = "listarAll")
    List<Tarea> listar();

    @WebMethod(operationName = "buscar")
    Tarea buscar(@WebParam(name = "identificador") Long id);

    @WebMethod(operationName = "crear")
    Tarea crear(String titulo, String descripcion);

    @WebMethod(operationName = "actualizar")
    Tarea actualizar(Long id, String titulo, String descripcion, Boolean completada);

    @WebMethod(operationName = "eliminar")
    boolean eliminar(Long id);
}
