package com.docencia.tasks.infrastructure.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class ApplicationConfigUnitTest {
    @Test
    void passwordEncoder_shouldBeBCrypt() {
        ApplicationConfig config = new ApplicationConfig();

        PasswordEncoder encoder = config.passwordEncoder();

        Assertions.assertNotNull(encoder);
        Assertions.assertTrue(encoder.matches("pass", encoder.encode("pass")));
    }
}
