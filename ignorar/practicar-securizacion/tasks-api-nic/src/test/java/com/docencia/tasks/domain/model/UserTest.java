package com.docencia.tasks.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class UserTest {
 @Test
    void constructor_withId_shouldSetId() {
        Long id = 42L;
        User user = new User(id);

        assertEquals(id, user.getId());
        assertNull(user.getUserName());
        assertNull(user.getPassword());
        assertNull(user.getRol());
    }

    @Test
    void rol_getterAndSetter_shouldWork() {
        User user = new User();
        String rol = "ADMIN";

        user.setRol(rol);
        assertEquals(rol, user.getRol());
    }

    @Test
    void password_getterAndSetter_shouldWork() {
        User user = new User();
        String password = "1234";

        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    
}
