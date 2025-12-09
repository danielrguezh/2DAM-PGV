package com.docencia.tareas.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "tarea")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tarea {

    private Long id;
    private String titulo;
    private String descripcion;
    private boolean completada;

    /**
     * Constructor vacia
     */
    public Tarea() {
    }

    /**
     * Constructor con identificador
     * @param id unica de la tarea
     */
    public Tarea(Long id) {
        this.id = id;
    }

    /**
     * Constructor con todas las propiedades
     * @param id unica de la tarea
     * @param titulo de la tarea
     * @param descripcion de la tarea
     * @param completada estado de la tarea: true/false dependiendo de si está completada o no
     */
    public Tarea(Long id, String titulo, String descripcion, boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = completada;
    }

    // getters y setters...


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return this.completada;
    }

    public boolean getCompletada() {
        return this.completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Tarea)) {
            return false;
        }
        Tarea tarea = (Tarea) o;
        return Objects.equals(id, tarea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}