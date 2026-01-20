package com.docencia.tasks.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Clase de autenticacion
 * @author prorix
 * @version 1.0.0
 */
@Service
public class AuthService {

    private final JpaUserDetailsService userDetailsService;
    private final JwtService jwtService;


    public AuthService(JpaUserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Metodo que genera un token
     * 
     * @param userName nombre de usuario
     * @param password contraseña
     * @return token
     */
    public String login(String userName, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(userName);
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }
        return jwtService.generateToken(user.getUsername(), user.getAuthorities());
    }
}
