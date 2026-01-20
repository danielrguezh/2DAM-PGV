package com.docencia.tasks.domain.model;

/**
 * @author nexphernandez
 * @version 1.0.0
 */
public class User {

    private Long id;
    private String userName;
    private String password;
    private String rol;

    /**
     * Constructor vacio
     */
    public User() {
    }

    /**
     * Consturctor con el identificador de la clase
     *
     * @param id identificador del usuario
     */
    public User(Long id) {
        this.id = id;
    }

    /**
     * Constructor con los atributos de la clase
     *
     * @param id identificador del usuario
     * @param userName nombre de usuario
     * @param password contrasenia del usuario
     * @param rol rol del usuario
     */
    public User(Long id, String userName, String password, String rol) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.rol = rol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
