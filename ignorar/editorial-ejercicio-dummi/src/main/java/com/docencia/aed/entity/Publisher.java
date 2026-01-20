package com.docencia.aed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
@Entity
@Table(name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "city", length = 100)
    private String city;

    /**
     * Cosntructor vacio
     */
    public Publisher() {
    }
    /**
     * Constructor con id
     * @param id unica de la editorial
     */
    public Publisher(Long id) {
        this.id = id;
    }
    /**
     * Constructor con propiedades
     * @param id unica de la editorial
     * @param name de la editorial
     * @param city de la editorial
     */
    public Publisher(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Publisher)) {
            return false;
        }
        Publisher publisher = (Publisher) o;
        return Objects.equals(id, publisher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
