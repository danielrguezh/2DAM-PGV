package com.docencia.tasks.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OpenApiConfig.class)
class OpenApiConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void openApiBean_shouldBeCreated() {
        assertNotNull(openAPI);
    }

    @Test
    void openApi_shouldContainSecurityRequirement() {
        assertNotNull(openAPI.getSecurity());
        assertFalse(openAPI.getSecurity().isEmpty());
        boolean containsBearerAuth = openAPI.getSecurity().stream()
                .anyMatch(sr -> sr.containsKey("bearerAuth"));

        assertTrue(containsBearerAuth);
    }

    @Test
    void openApi_shouldContainBearerSecurityScheme() {
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());

        SecurityScheme scheme = openAPI.getComponents()
                .getSecuritySchemes()
                .get("bearerAuth");

        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
        assertEquals("bearerAuth", scheme.getName());
    }
}
