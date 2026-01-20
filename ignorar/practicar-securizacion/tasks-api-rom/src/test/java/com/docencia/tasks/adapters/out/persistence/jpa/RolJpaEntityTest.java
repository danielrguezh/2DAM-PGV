package com.docencia.tasks.adapters.out.persistence.jpa;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class RolJpaEntityTest {

    private static final String ROL = "Soy un Rol";
    private static final long ID_INICIAL = 1l;
    private static final Set<UserJpaEntity> USERS = new HashSet<>();

    RolJpaEntity jpaEntity;
    RolJpaEntity rol;
    

    public RolJpaEntityTest() {
    }

    @BeforeEach
    void setUp(){
        jpaEntity = new RolJpaEntity();
        rol = new RolJpaEntity(1L,ROL);
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
        jpaEntity.setRol(ROL);
        jpaEntity.setUsers(USERS);

        Assertions.assertEquals(ID_INICIAL, jpaEntity.getId());
        Assertions.assertEquals(ROL, jpaEntity.getRol());
        Assertions.assertEquals(USERS, jpaEntity.getUsers());
    }

    @Test
    @Order(3)
    void jpaEntityEqualTest(){
        Assertions.assertEquals(rol, new RolJpaEntity(ID_INICIAL));
    }

    @Test
    @Order(4)
    void jpaEntityNotInstanceTest(){
        Assertions.assertNotEquals(rol, "rol");
    }

    @Test
    @Order(5)
    void jpaEntitySameObjectTest(){
        Assertions.assertEquals(rol, rol);
    }

    @Test
    void hashCodeTest() {

    RolJpaEntity rol2 = new RolJpaEntity(1L, "ADMIN");
    RolJpaEntity rol3 = new RolJpaEntity(2L, "USER");

    Assertions.assertEquals(rol.hashCode(), rol2.hashCode());

    Assertions.assertNotEquals(rol.hashCode(), rol3.hashCode());
}
}
