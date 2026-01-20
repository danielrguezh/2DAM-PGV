package com.docencia.tasks.adapters.in.controller;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.docencia.tasks.infrastructure.security.AuthService;
import com.docencia.tasks.infrastructure.security.JwtService;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void login_withValidCredentials_shouldReturnToken() {
        String username = "usuario";
        String password = "contrasenia";
        String fakeToken = "fake.jwt.token";
    
        when(authService.login(username, password)).thenReturn(fakeToken);
        when(jwtService.extractRoles(fakeToken)).thenReturn(List.of("ROLE_USER"));
    
        ResponseEntity<?> response = authController.login(username, password);
    
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("token")).isEqualTo(fakeToken);
    }
    

    @Test
    void login_withInvalidCredentials_shouldThrowUnauthorized() {
        String username = "usuario";
        String password = "contraseniaIncorrecta";

        when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Credenciales invalidas"));

        try {
            authController.login(username, password);
        } catch (ResponseStatusException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(ex.getReason()).isEqualTo("Credenciales invalidas");
        }
    }
}