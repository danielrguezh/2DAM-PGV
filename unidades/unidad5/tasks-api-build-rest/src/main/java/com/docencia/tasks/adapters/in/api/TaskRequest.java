package com.docencia.tasks.adapters.in.api;
import java.util.Objects;

public class TaskRequest {

    private Long identificador;
    private String titulo;
    private String descripcion;
    private boolean completado;


    public TaskRequest() {
    }

    public TaskRequest(Long identificador) {
        this.identificador = identificador;
    }

    public TaskRequest(Long identificador, String titulo, String descripcion, boolean completado) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completado = completado;
    }

    public Long getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
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

    public boolean isCompletado() {
        return this.completado;
    }

    public boolean getCompletado() {
        return this.completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TaskRequest)) {
            return false;
        }
        TaskRequest taskRequest = (TaskRequest) o;
        return Objects.equals(identificador, taskRequest.identificador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }

    @Override
    public String toString() {
        return "{" +
            " identificador='" + getIdentificador() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", completado='" + isCompletado() + "'" +
            "}";
    }
    
}
