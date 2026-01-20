package com.docencia.tasks.adapters.out.persistence.jpa;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Clase jpa de rol
 * @author prorix
 * @version 1.0.0
 */
@Entity
@Table(name = "Roles")
public class RolJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rol", nullable = false, unique = true)
    private String rol;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserJpaEntity> users;

    /**
     * Contructor vacio
     */
    public RolJpaEntity() {
    }

    /**
     * Constructor solo con la clave primaria
     * 
     * @param id id del rol
     */
    public RolJpaEntity(Long id) {
        this.id = id;
    }

    /**
     * Constructor con todos los parametros
     * 
     * @param id  id del rol
     * @param rol nombre del rol
     */
    public RolJpaEntity(Long id, String rol) {
        this.id = id;
        this.rol = rol;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return this.rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Set<UserJpaEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserJpaEntity> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RolJpaEntity)) {
            return false;
        }
        RolJpaEntity rolJpaEntity = (RolJpaEntity) o;
        return Objects.equals(id, rolJpaEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
