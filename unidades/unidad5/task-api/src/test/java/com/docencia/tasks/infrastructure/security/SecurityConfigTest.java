package com.docencia.tasks.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Test
    void contextLoads_andSecurityFilterChainExists() {
        assertThat(context).isNotNull();
        assertThat(springSecurityFilterChain).isNotNull();
    }

    @Test
    void publicAuthEndpoint_shouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void swaggerEndpoints_shouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void h2Console_shouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorized_whenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtAuthenticationFilter_shouldBePresentInFilterChain() {
        boolean filterExists = springSecurityFilterChain.getFilterChains()
                .stream()
                .flatMap(chain -> chain.getFilters().stream())
                .anyMatch(filter -> filter instanceof JwtAuthenticationFilter);

        assertThat(filterExists).isTrue();
    }
}
