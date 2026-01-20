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
        UserResponse ur = new UserResponse(1L, "user", "pass");
        assertThat(ur.getId()).isEqualTo(1L);
        assertThat(ur.getUserName()).isEqualTo("user");
        assertThat(ur.getPassword()).isEqualTo("pass");
    }

    @Test
    void settersAndGetters_shouldWork() {
        UserResponse ur = new UserResponse();
        ur.setId(10L);
        ur.setUserName("john");
        ur.setPassword("1234");

        assertThat(ur.getId()).isEqualTo(10L);
        assertThat(ur.getUserName()).isEqualTo("john");
        assertThat(ur.getPassword()).isEqualTo("1234");
    }

    @Test
    void equals_sameId_shouldBeEqual() {
        UserResponse ur1 = new UserResponse(1L, "A", "B");
        UserResponse ur2 = new UserResponse(1L, "X", "Y");

        assertThat(ur1).isEqualTo(ur2);
    }

    @Test
    void equals_differentId_shouldNotBeEqual() {
        UserResponse ur1 = new UserResponse(1L, "A", "B");
        UserResponse ur2 = new UserResponse(2L, "A", "B");

        assertThat(ur1).isNotEqualTo(ur2);
    }

    @Test
    void equals_nullOrOtherType_shouldNotBeEqual() {
        UserResponse ur = new UserResponse(1L, "A", "B");

        assertThat(ur).isNotEqualTo(null);
        assertThat(ur).isNotEqualTo("string");
    }

    @Test
    void hashCode_sameId_shouldBeEqual() {
        UserResponse ur1 = new UserResponse(1L);
        UserResponse ur2 = new UserResponse(1L);

        assertThat(ur1.hashCode()).isEqualTo(ur2.hashCode());
    }

    @Test
    void hashCode_differentId_shouldNotBeEqual() {
        UserResponse ur1 = new UserResponse(1L);
        UserResponse ur2 = new UserResponse(2L);

        assertThat(ur1.hashCode()).isNotEqualTo(ur2.hashCode());
    }
}