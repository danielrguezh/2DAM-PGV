package com.docencia.tasks.infraestructure.security;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.docencia.tasks.infrastructure.security.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET
            = "12312312312312312312312312312312";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 5);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtService.generateToken(
                "usuario",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
    }

    @Test
    void extractUsername_shouldReturnUsername() {
        String token = jwtService.generateToken(
                "usuario",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String username = jwtService.extractUsername(token);

        assertEquals("usuario", username);
    }

    @Test
    void extractRoles_shouldReturnRoles() {
        String token = jwtService.generateToken(
                "usuario",
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        );

        Collection<String> roles = jwtService.extractRoles(token);

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }


    @Test
    void extractRoles_shouldReturnNull_whenRolesNotACollection() {
        String token = Jwts.builder()
                .claim("roles", "NOT_A_COLLECTION")
                .subject("usuario")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();

        Collection<String> roles = jwtService.extractRoles(token);

        assertNull(roles); 
    }

    @Test
    void isValid_shouldReturnFalse_whenTokenExpired() throws InterruptedException {
        JwtService shortJwt = new JwtService(SECRET, 0); 

        String token = shortJwt.generateToken(
                "usuario",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Thread.sleep(1000); 

        assertFalse(shortJwt.isValid(token));
    }

    @Test
    void isValid_shouldReturnFalse_whenTokenMalformed() {
        String invalidToken = "esto.no.es.un.jwt";

        assertFalse(jwtService.isValid(invalidToken));
    }

    @Test
    void extractUsername_shouldThrowException_whenTokenInvalid() {
        String invalidToken = "token.invalido";

        assertThrows(Exception.class, ()
                -> jwtService.extractUsername(invalidToken)
        );
    }
}
