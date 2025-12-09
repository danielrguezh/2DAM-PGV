package com.docencia.tareas.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.docencia.tareas.model.Tarea;
import com.docencia.tareas.repositories.ITareaRepository;

@Service
public class TareaService implements ITareaService{

    ITareaRepository tareaRepository;

    public TareaService(ITareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public List<Tarea> listarTodas() {
        return tareaRepository.all();
    }

    @Override
    public Tarea buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        Tarea tarea = new Tarea(id);
        return tareaRepository.findBy(tarea);
    }

    @Override
    public Tarea crearTarea(String titulo, String descripcion) {
        long id = UUID.randomUUID().getLeastSignificantBits();
        Tarea tarea = new Tarea(id, titulo, descripcion, false);
        return tareaRepository.add(tarea);

    }

    @Override
    public Tarea actualizarTarea(Long id, String titulo, String descripcion, Boolean completada) {
        if (id == null) {
            return null;
        }
        
        Tarea tarea = new Tarea(id, titulo, descripcion, completada);
        return tareaRepository.update(tarea);
    }

    @Override
    public boolean eliminarTarea(Long id) {
        if (id == null) {
            return false;
        }
        Tarea tarea = new Tarea(id);
        return tareaRepository.delete(tarea);
    }

}
