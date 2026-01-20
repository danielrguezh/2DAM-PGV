package com.docencia.tasks.adapters.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.docencia.tasks.adapters.in.api.UserRequest;
import com.docencia.tasks.adapters.in.api.UserResponse;
import com.docencia.tasks.adapters.mapper.UserMapperImpl;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.domain.model.User;

class UserMapperImplTest {

    private UserMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
    }

    // ----------------- toDomain(UserRequest) -----------------
    @Test
    void toDomain_fromRequest_shouldMapAllFields() {
        UserRequest request = new UserRequest();
        request.setUserName("nico");
        request.setPassword("1234");

        User user = mapper.toDomain(request);

        assertEquals("nico", user.getUserName());
        assertEquals("1234", user.getPassword());
    }

    @Test
    void toDomain_fromRequest_nullRequest_shouldReturnNull() {
        User user = mapper.toDomain((UserRequest) null);
        assertNull(user);
    }

    // ----------------- toResponse(User) -----------------
    @Test
    void toResponse_fromUser_shouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setUserName("nico");
        user.setPassword("1234");

        UserResponse response = mapper.toResponse(user);

        assertEquals(1L, response.getId());
        assertEquals("nico", response.getUserName());
        assertEquals("1234", response.getPassword());
    }

    @Test
    void toResponse_nullUser_shouldReturnNull() {
        UserResponse response = mapper.toResponse(null);
        assertNull(response);
    }

    // ----------------- toJpa(User) -----------------
    @Test
    void toJpa_fromUser_shouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setUserName("nico");
        user.setPassword("1234");

        UserJpaEntity entity = mapper.toJpa(user);

        assertEquals(1L, entity.getId());
        assertEquals("nico", entity.getUserName());
        assertEquals("1234", entity.getPassword());
    }

    @Test
    void toJpa_nullUser_shouldReturnNull() {
        UserJpaEntity entity = mapper.toJpa(null);
        assertNull(entity);
    }

    // ----------------- toDomain(UserJpaEntity) -----------------
    @Test
    void toDomain_fromJpaEntity_shouldMapAllFields() {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(1L);
        entity.setUserName("nico");
        entity.setPassword("1234");

        User user = mapper.toDomain(entity);

        assertEquals(1L, user.getId());
        assertEquals("nico", user.getUserName());
        assertEquals("1234", user.getPassword());
    }

    @Test
    void toDomain_nullJpaEntity_shouldReturnNull() {
        User user = mapper.toDomain((UserJpaEntity) null);
        assertNull(user);
    }

    // ----------------- updateDomainFromRequest -----------------
    @Test
    void updateDomainFromRequest_shouldUpdateUserName() {
        User user = new User();
        user.setUserName("original");
        user.setPassword("originalPass");

        UserRequest request = new UserRequest();
        request.setUserName("nuevoNombre");

        mapper.updateDomainFromRequest(request, user);

        assertEquals("nuevoNombre", user.getUserName());
        assertEquals("originalPass", user.getPassword());
    }

    @Test
    void updateDomainFromRequest_shouldUpdatePassword() {
        User user = new User();
        user.setUserName("original");
        user.setPassword("originalPass");

        UserRequest request = new UserRequest();
        request.setPassword("nuevoPass");

        mapper.updateDomainFromRequest(request, user);

        assertEquals("original", user.getUserName());
        assertEquals("nuevoPass", user.getPassword());
    }

    @Test
    void updateDomainFromRequest_shouldNotChangeWhenNull() {
        User user = new User();
        user.setUserName("original");
        user.setPassword("originalPass");

        UserRequest request = new UserRequest(); // ambos null

        mapper.updateDomainFromRequest(request, user);

        assertEquals("original", user.getUserName());
        assertEquals("originalPass", user.getPassword());
    }

    @Test
    void updateDomainFromRequest_nullRequest_shouldDoNothing() {
        User user = new User();
        user.setUserName("original");
        user.setPassword("originalPass");

        mapper.updateDomainFromRequest(null, user);

        assertEquals("original", user.getUserName());
        assertEquals("originalPass", user.getPassword());
    }
}