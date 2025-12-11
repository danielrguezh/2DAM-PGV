package com.docencia.tareas.model;

import jakarta.xml.bind.annotation.*;
import java.util.Objects;

@XmlRootElement(name = "alumno")
@XmlAccessorType(XmlAccessType.FIELD)
public class Alumno {
    private Long identificador;
    private String nombre;

    public Alumno() {
    }

    public Alumno(Long identificador) {
        this.identificador = identificador;
    }

    public Alumno(Long identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    public Long getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Alumno)) {
            return false;
        }
        Alumno alumno = (Alumno) o;
        return Objects.equals(identificador, alumno.identificador) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }
}
