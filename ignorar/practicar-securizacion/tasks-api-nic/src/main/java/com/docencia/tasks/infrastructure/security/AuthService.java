package com.docencia.tasks.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * @author nexphernandez
 * @version 1.0.0
 */
@Service
public class AuthService {

    private final JpaUserDetailsService userDetailsService;
    private final JwtService jwtService;
    /**
     * Contructor para inicializar la clase
     * @param userDetailsService inyeccion de los detalles de usuario
     * @param jwtService inyeccion del servicio
     */
    public AuthService(JpaUserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Metodo para generar un token 
     * @param username nombre de usuario
     * @param password contraseña
     * @return token
     */
    public String login(String username, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtService.generateToken(user.getUsername(), user.getAuthorities());
    }
}
