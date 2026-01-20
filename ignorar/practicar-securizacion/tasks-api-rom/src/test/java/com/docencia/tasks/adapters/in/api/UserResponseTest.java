package com.docencia.tasks.adapters.in.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class UserResponseTest {

    @Test
    void constructorVacio_shouldCreateObject() {
        UserResponse ur = new UserResponse();
        assertThat(ur).isNotNull();
    }

    @Test
    void constructorConId_shouldSetId() {
        UserResponse ur = new UserResponse(5L);
        assertThat(ur.getId()).isEqualTo(5L);
    }

    @Test
    void constructorCompleto_shouldSetAllFields() {
        UserResponse ur = new UserResponse(10L, "admin", "adminpass");
        assertThat(ur.getId()).isEqualTo(10L);
        assertThat(ur.getUserName()).isEqualTo("admin");
        assertThat(ur.getPassword()).isEqualTo("adminpass");
    }

    @Test
    void settersAndGetters_shouldWork() {
        UserResponse ur = new UserResponse();
        ur.setId(10L);
        ur.setUserName("admin2");
        ur.setPassword("adminpass2");

        assertThat(ur.getId()).isEqualTo(10L);
        assertThat(ur.getUserName()).isEqualTo("admin2");
        assertThat(ur.getPassword()).isEqualTo("adminpass2");
    }

    @Test
    void equals_sameId_shouldBeEqual() {
        UserResponse ur1 = new UserResponse(10L, "A", "B");
        UserResponse ur2 = new UserResponse(10L, "X", "Y");

        assertThat(ur1).isEqualTo(ur2);
    }

    @Test
    void equals_differentId_shouldNotBeEqual() {
        UserResponse ur1 = new UserResponse(10L, "A", "B");
        UserResponse ur2 = new UserResponse(20L, "A", "B");

        assertThat(ur1).isNotEqualTo(ur2);
    }

    @Test
    void equals_nullOrOtherType_shouldNotBeEqual() {
        UserResponse ur = new UserResponse(10L, "A", "B");

        assertThat(ur).isNotEqualTo(null);
        assertThat(ur).isNotEqualTo("string");
    }

    @Test
    void hashCode_sameId_shouldBeEqual() {
        UserResponse ur1 = new UserResponse(10L);
        UserResponse ur2 = new UserResponse(10L);

        assertThat(ur1.hashCode()).isEqualTo(ur2.hashCode());
    }

    @Test
    void hashCode_differentId_shouldNotBeEqual() {
        UserResponse ur1 = new UserResponse(10L);
        UserResponse ur2 = new UserResponse(20L);

        assertThat(ur1.hashCode()).isNotEqualTo(ur2.hashCode());
    }
}