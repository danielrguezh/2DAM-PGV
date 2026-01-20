package com.docencia.tasks.adapters.in.api;
import java.util.Objects;
/**
 * @author nexphernandez
 * @version 1.0.0
 */
public class UserResponse {
    private Long id;
    private String userName;
    private String password;

    /**
    * Constructor vacio
    */
    public UserResponse() {
    }
    
    /**
     * Consturctor con el identificador de la clase
     * @param id identificador del usuario
     */
    public UserResponse(Long id) {
        this.id = id;
    }

    /**
     * Constructor con los atributos de la clase
     * @param id identificador del usuario
     * @param userName nombre de usuario
     * @param password contrasenia del usuario
     */
    public UserResponse(Long id,String userName, String password) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserResponse)) {
            return false;
        }
        UserResponse userResponse = (UserResponse) o;
        return Objects.equals(id, userResponse.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
