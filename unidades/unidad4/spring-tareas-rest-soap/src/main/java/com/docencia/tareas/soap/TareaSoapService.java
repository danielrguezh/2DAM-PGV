package com.docencia.tareas.soap;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docencia.tareas.model.Tarea;
import com.docencia.tareas.service.ITareaService;
import com.docencia.tareas.soap.interfaces.ITareaSoapService;

import jakarta.jws.WebService;

@WebService(
    serviceName = "TareaService",
    portName = "TareaPort",
    targetNamespace = "http://tareas.ies.puerto.es/",
    endpointInterface = "com.docencia.tareas.soap.interfaces.ITareaSoapService"
)
@Service
public class TareaSoapService implements ITareaSoapService{
    private final ITareaService tareaService;

    public TareaSoapService(ITareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Override
    public List<Tarea> listar() {
        return tareaService.listarTodas();
    }

    @Override
    public Tarea buscar(Long id) {
        return tareaService.buscarPorId(id);
    }

    @Override
    public Tarea crear(String titulo, String descripcion) {
        return tareaService.crearTarea(titulo, descripcion);
    }

    @Override
    public Tarea actualizar(Long id, String titulo, String descripcion, Boolean completada) {
        return tareaService.actualizarTarea(id, titulo, descripcion, completada);
    }

    @Override
    public boolean eliminar(Long id) {
       return tareaService.eliminarTarea(id);
    }
}
