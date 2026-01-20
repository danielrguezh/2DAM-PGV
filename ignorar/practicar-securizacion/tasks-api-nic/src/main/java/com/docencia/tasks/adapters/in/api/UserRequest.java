package com.docencia.tasks.adapters.in.api;
/**
 * @author nexphernandez
 * @version 1.0.0
 */
public class UserRequest {
    private String userName;
    private String password;
    
    /**
    * Constructor vacio
    */
    public UserRequest() {
    }
    
    /**
     * Constructor con los atributos de la clase
     * @param userName nombre de usuario
     * @param password contrasenia del usuario
     */
    public UserRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
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
    
    
}
