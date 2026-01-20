package com.docencia.tasks.adapters.out.persistence.jpa;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class UserJpaEntityTest {

    private static final String USER_NAME = "Soy un nombre";
    private static final String PASSWORD = "Soy una contraseña";
    private static final long ID_INICIAL = 1l;
    private static final Set<RolJpaEntity> ROLES = new HashSet<>();

    UserJpaEntity jpaEntity;
    UserJpaEntity user;
    

    public UserJpaEntityTest() {
    }

    @BeforeEach
    void setUp(){
        jpaEntity = new UserJpaEntity();
        user = new UserJpaEntity(1L,USER_NAME,PASSWORD);
    }

    @Test
    @Order(1)
    void jpaEntityNotNullTest(){
        Assertions.assertNotNull(jpaEntity, "El valor no puede ser nulo");
    }

    @Test
    @Order(2)
    void jpaEntityGetsSetsTest(){

        jpaEntity.setId(ID_INICIAL);
        jpaEntity.setUserName(USER_NAME);
        jpaEntity.setPassword(PASSWORD);
        jpaEntity.setRoles(ROLES);

        Assertions.assertEquals(ID_INICIAL, jpaEntity.getId());
        Assertions.assertEquals(USER_NAME, jpaEntity.getUserName());
        Assertions.assertEquals(PASSWORD, jpaEntity.getPassword());
        Assertions.assertEquals(ROLES, jpaEntity.getRoles());
    }

    @Test
    @Order(3)
    void jpaEntityEqualTest(){
        Assertions.assertEquals(user, new UserJpaEntity(ID_INICIAL));
    }

    @Test
    @Order(4)
    void jpaEntityNotInstanceTest(){
        Assertions.assertNotEquals(user, "user");
    }

    @Test
    @Order(5)
    void jpaEntitySameObjectTest(){
        Assertions.assertEquals(user, user);
    }

    @Test
    void hashCodeTest() {

    UserJpaEntity user2 = new UserJpaEntity(1L, "ADMIN","123");
    UserJpaEntity user3 = new UserJpaEntity(2L, "ADMIN","123");

    Assertions.assertEquals(user.hashCode(), user2.hashCode());

    Assertions.assertNotEquals(user.hashCode(), user3.hashCode());
}
}
