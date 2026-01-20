package com.docencia.tasks.adapters.in.api;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class UserRequestTest {

    @Test
    void constructorVacio_shouldCreateObject() {
        UserRequest ur = new UserRequest();
        assertThat(ur).isNotNull();
    }

    @Test
    void constructorCompleto_shouldSetAllFields() {
        UserRequest ur = new UserRequest("nicolas", "1234");
        assertThat(ur.getUserName()).isEqualTo("nicolas");
        assertThat(ur.getPassword()).isEqualTo("1234");
    }

    @Test
    void settersAndGetters_shouldWork() {
        UserRequest ur = new UserRequest();
        ur.setUserName("admin");
        ur.setPassword("adminpass");

        assertThat(ur.getUserName()).isEqualTo("admin");
        assertThat(ur.getPassword()).isEqualTo("adminpass");
    }
}