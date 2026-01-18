package com.docencia.tasks.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET =
            "my-super-secret-key-my-super-secret-key";

    private static final long EXPIRATION_MINUTES = 10;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRATION_MINUTES);
    }

    @Test
    void generateToken_shouldReturnValidJwt() {
        String token = jwtService.generateToken("user@test.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(token.split("\\.").length == 3); 
    }

    @Test
    void extractUsername_shouldReturnCorrectSubject() {
        String token = jwtService.generateToken("user@test.com");

        String username = jwtService.extractUsername(token);

        assertEquals("user@test.com", username);
    }

    @Test
    void isTokenValid_shouldReturnTrue_forValidTokenAndUser() {
        String username = "user@test.com";
        String token = jwtService.generateToken(username);

        UserDetails userDetails = new User(
                username,
                "password",
                Collections.emptyList()
        );

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
        String token = jwtService.generateToken("user@test.com");

        UserDetails otherUser = new User(
                "other@test.com",
                "password",
                Collections.emptyList()
        );

        boolean valid = jwtService.isTokenValid(token, otherUser);

        assertFalse(valid);
    }

    /**
     * @Test
    void token_shouldExpireImmediately_whenExpirationIsZero() throws InterruptedException {
        JwtService shortLivedJwtService = new JwtService(SECRET, 0);

        Thread.sleep(10);

        UserDetails userDetails = new User(
                "user@test.com",
                "password",
                Collections.emptyList()
        );

        boolean valid = shortLivedJwtService.isTokenValid(token, userDetails);

        assertFalse(valid);
    }
     */

    @Test
    void extractUsername_shouldThrowException_forInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThrows(Exception.class, () ->
                jwtService.extractUsername(invalidToken)
        );
    }
}
