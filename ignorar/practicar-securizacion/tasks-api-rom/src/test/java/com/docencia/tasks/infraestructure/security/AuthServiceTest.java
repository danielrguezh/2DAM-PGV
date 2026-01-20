package com.docencia.tasks.infraestructure.security;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.docencia.tasks.infrastructure.security.AuthService;
import com.docencia.tasks.infrastructure.security.JpaUserDetailsService;
import com.docencia.tasks.infrastructure.security.JwtService;

class AuthServiceTest {

    JpaUserDetailsService userDetailsService;
    JwtService jwtService;
    AuthService authService;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(JpaUserDetailsService.class);
        jwtService = mock(JwtService.class);
        authService = new AuthService(userDetailsService, jwtService);
    }

    @Test
    void login_correctCredentials_shouldReturnToken() {
        String username = "usuario";
        String rawPassword = "contrasenia";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
        when(jwtService.generateToken(eq(username), any())).thenReturn("mocked-token");

        String token = authService.login(username, rawPassword);

        assertNotNull(token);
        assertEquals("mocked-token", token);

        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtService).generateToken(eq(username), any());
    }

    @Test
    void login_incorrectPassword_shouldThrow() {
        String username = "usuario";
        String rawPassword = "aaaaaaa";
        String encodedPassword = new BCryptPasswordEncoder().encode("contrasenia");
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .authorities(List.of())
                .build();

        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(username, rawPassword));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtService, never()).generateToken(any(), any());
    }
}