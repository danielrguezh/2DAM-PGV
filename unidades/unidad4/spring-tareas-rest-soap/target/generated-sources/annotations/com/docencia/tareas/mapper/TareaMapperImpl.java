package com.docencia.tareas.mapper;

import com.docencia.tareas.model.Alumno;
import com.docencia.tareas.model.Tarea;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-11T17:09:01+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
public class TareaMapperImpl implements TareaMapper {

    @Override
    public Alumno toAlumno(Tarea tarea) {
        if ( tarea == null ) {
            return null;
        }

        Alumno alumno = new Alumno();

        alumno.setIdentificador( tarea.getId() );
        alumno.setNombre( tarea.getTitulo() );

        return alumno;
    }

    @Override
    public List<Alumno> toAlumnos(List<Tarea> tareas) {
        if ( tareas == null ) {
            return null;
        }

        List<Alumno> list = new ArrayList<Alumno>( tareas.size() );
        for ( Tarea tarea : tareas ) {
            list.add( toAlumno( tarea ) );
        }

        return list;
    }
}
