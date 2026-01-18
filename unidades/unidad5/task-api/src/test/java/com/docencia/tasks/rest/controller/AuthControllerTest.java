package com.docencia.tasks.rest.controller;

import com.docencia.tasks.infrastructure.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        String username = "user";
        String password = "pass";
        String jwt = "jwt-token-value";

        Mockito.when(jwtService.generateToken(username))
                .thenReturn(jwt);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "user",
                                  "password": "pass"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwt));
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreInvalid() throws Exception {
        Mockito.doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "user",
                                  "password": "wrong"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldFail_whenRequestBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
     */
}
