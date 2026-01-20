package com.docencia.tasks.adapters.out.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.docencia.tasks.adapters.mapper.UserMapper;
import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.RolJpaRepository;
import com.docencia.tasks.adapters.out.persistence.repository.UserJpaRepository;
import com.docencia.tasks.domain.model.User;

public class UserPersistenceAdapterTest {

    RolJpaEntity rolEntiyt;
    UserJpaEntity userEntity;
    UserJpaRepository userJpa;
    RolJpaRepository rolJpa;
    UserPersistenceAdapter userPersistenceAdapter;
    UserMapper mapper;
    User user;

    @BeforeEach
    void setUp() {
        userJpa = mock(UserJpaRepository.class);
        rolJpa = mock(RolJpaRepository.class);
        mapper = mock(UserMapper.class);
        userPersistenceAdapter = new UserPersistenceAdapter(userJpa, mapper, rolJpa);
        userEntity = new UserJpaEntity(1L, "user", "user123");
        user = new User(1L, "user", "user123", null);
        rolEntiyt = new RolJpaEntity(1L, "USER");
    }

    @Test
    void getAll() {
        when(userJpa.findAll()).thenReturn(List.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);
        List<User> result = userPersistenceAdapter.getAll();
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userJpa).findAll();
        verify(mapper).toDomain(userEntity);
    }

    @Test
    void getById() {
        when(userJpa.findById(1l)).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);
        Optional<User> result = userPersistenceAdapter.getById(1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userJpa).findById(1L);
        verify(mapper).toDomain(userEntity);
    }

    @Test
    void deleteUserIdNull() {
        boolean result = userPersistenceAdapter.deleteById(null);
        assertFalse(result);
    }

    @Test
    void deleteUserTest() {
        boolean result = userPersistenceAdapter.deleteById(user.getId());
        assertTrue(result);
    }

    @Test
    void saveUserWithoutRolesTest() {
        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(rolJpa.findByRol("USER")).thenReturn(Optional.of(rolEntiyt));
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.save(user, "USER");

        assertNotNull(result);
        verify(userJpa).save(userEntity);
        verify(rolJpa).findByRol("USER");
    }

    @Test
    void saveUserWithRolesTest() {
        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(rolJpa.findByRol("USER")).thenReturn(Optional.of(rolEntiyt));
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.save(user, "USER");

        assertNotNull(result);
        verify(rolJpa).findByRol("USER");
        verify(userJpa).save(userEntity);
    }

    @Test
    void updateUserNotFoundTest() {
        when(userJpa.findById(user.getId())).thenReturn(Optional.empty());
        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(rolJpa.findByRol("USER")).thenReturn(Optional.of(rolEntiyt));
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.update(user, "USER");

        assertNotNull(result);
        verify(rolJpa).findByRol("USER");
        verify(userJpa).save(userEntity);
    }

    @Test
    void updateUserWithNullRolesTest() {
        userEntity.setRoles(null);

        when(userJpa.findById(user.getId())).thenReturn(Optional.of(userEntity));
        when(rolJpa.findByRol("USER")).thenReturn(Optional.of(rolEntiyt));
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.update(user, "USER");

        assertNotNull(result);
        verify(userJpa).save(userEntity);
    }

    @Test
    void updateUserWithExistingRolesTest() {
        userEntity.setRoles(new HashSet<>());

        when(userJpa.findById(user.getId())).thenReturn(Optional.of(userEntity));
        when(rolJpa.findByRol("USER")).thenReturn(Optional.of(rolEntiyt));
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.update(user, "USER");

        assertNotNull(result);
        verify(userJpa).save(userEntity);
    }

    @Test
    void saveUserWithRoleTest() {
        userEntity.setRoles(new HashSet<>());

        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(rolJpa.findByRol("USER")).thenReturn(Optional.of(rolEntiyt));
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.save(user, "USER");

        assertNotNull(result);
        verify(rolJpa).findByRol("USER");
        verify(userJpa).save(userEntity);
    }

    @Test
    void saveUserWithNullRol_doesNothing() {
        userEntity.setRoles(new HashSet<>());

        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.save(user, null);

        assertNotNull(result);
        assertTrue(userEntity.getRoles().isEmpty());

        verify(rolJpa, never()).findByRol(any());
    }

    @Test
    void saveUserWithBlankRol_doesNothing() {
        userEntity.setRoles(new HashSet<>());

        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(userJpa.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(user);

        User result = userPersistenceAdapter.save(user, " ");

        assertNotNull(result);
        assertTrue(userEntity.getRoles().isEmpty());

        verify(rolJpa, never()).findByRol(any());
    }

    @Test
    void saveUserWithNonExistingRol_throwsException() {
        userEntity.setRoles(new HashSet<>());

        when(mapper.toJpa(user)).thenReturn(userEntity);
        when(rolJpa.findByRol("ADMIN")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()
                -> userPersistenceAdapter.save(user, "ADMIN")
        );
    }
}
