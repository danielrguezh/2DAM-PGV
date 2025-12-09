package com.docencia.rest.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;


public class DetalleProducto {
    
    private int id;
    private int productoId;
    private String descripcionLarga;
    private Map<String, String> especificacionesTecnicas;
    private List<String> tags;


    public DetalleProducto() {
    }

    public DetalleProducto(int id, int productoId, String descripcionLarga, Map<String,String> especificacionesTecnicas, List<String> tags) {
        this.id = id;
        this.productoId = productoId;
        this.descripcionLarga = descripcionLarga;
        this.especificacionesTecnicas = especificacionesTecnicas;
        this.tags = tags;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return this.productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getDescripcionLarga() {
        return this.descripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }

    public Map<String,String> getEspecificacionesTecnicas() {
        return this.especificacionesTecnicas;
    }

    public void setEspecificacionesTecnicas(Map<String,String> especificacionesTecnicas) {
        this.especificacionesTecnicas = especificacionesTecnicas;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public DetalleProducto id(int id) {
        setId(id);
        return this;
    }

    public DetalleProducto productoId(int productoId) {
        setProductoId(productoId);
        return this;
    }

    public DetalleProducto descripcionLarga(String descripcionLarga) {
        setDescripcionLarga(descripcionLarga);
        return this;
    }

    public DetalleProducto especificacionesTecnicas(Map<String,String> especificacionesTecnicas) {
        setEspecificacionesTecnicas(especificacionesTecnicas);
        return this;
    }

    public DetalleProducto tags(List<String> tags) {
        setTags(tags);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DetalleProducto)) {
            return false;
        }
        DetalleProducto detalleProducto = (DetalleProducto) o;
        return id == detalleProducto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", productoId='" + getProductoId() + "'" +
            ", descripcionLarga='" + getDescripcionLarga() + "'" +
            ", especificacionesTecnicas='" + getEspecificacionesTecnicas() + "'" +
            ", tags='" + getTags() + "'" +
            "}";
    }
    
}
