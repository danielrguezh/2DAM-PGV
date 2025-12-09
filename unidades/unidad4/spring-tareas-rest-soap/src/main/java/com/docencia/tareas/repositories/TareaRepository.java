package com.docencia.tareas.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.docencia.tareas.model.Tarea;

@Repository
public class TareaRepository implements ITareaRepository {
    private final List<Tarea> tareas;


    public TareaRepository() {
        Tarea tarea1= new Tarea(1L, "Estudiar TypeScript", "Repasar tipos y funciones", false);
        Tarea tarea2= new Tarea(2L, "Hacer la practica 1", "Proyecto tareas en memoria", true);
        this.tareas = new ArrayList<>();
    }


    @Override
    public Tarea add(Tarea tarea) {
        if (!tareas.contains(tarea)) {
            return tarea;
        }
        tareas.add(tarea);
        return tarea;
    }


    @Override
    public boolean delete(Tarea tarea) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }


    @Override
    public Tarea findBy(Tarea tarea) {
        if (!tareas.contains(tarea)) {
            return null;
        }
        int posicion = tareas.indexOf(tarea);
        return tareas.get(posicion);
    }


    @Override
    public Tarea update(Tarea tarea) {
        int posicion = tareas.indexOf(tarea);

        if (posicion>-1) {
            tareas.set(posicion, tarea);
        }
        return tarea;
    }


    @Override
    public List<Tarea> all() {
        return tareas;
    }

}
