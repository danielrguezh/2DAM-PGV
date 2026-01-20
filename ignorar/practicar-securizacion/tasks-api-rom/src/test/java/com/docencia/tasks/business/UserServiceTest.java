package com.docencia.tasks.business;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.docencia.tasks.adapters.out.persistence.interfaces.IUserPersistenceAdapter;
import com.docencia.tasks.domain.model.User;

class UserServiceTest {

    @Mock
    private IUserPersistenceAdapter repo;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ok() {
        User input = new User();
        input.setId(10L);
        input.setUserName("usuario");

        User saved = new User();
        saved.setId(1L);
        saved.setUserName("usuario");

        when(repo.save(any(User.class), eq("USER"))).thenReturn(saved);

        User result = service.create(input, "USER");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repo).save(argThat(u -> u.getId() == null), eq("USER"));
    }

    @Test
    void getAll_ok() {
        when(repo.getAll()).thenReturn(List.of(new User(), new User()));

        List<User> result = service.getAll();

        assertEquals(2, result.size());
        verify(repo).getAll();
    }

    @Test
    void getById_found() {
        User user = new User();
        user.setId(1L);

        when(repo.getById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getById_notFound() {
        when(repo.getById(1L)).thenReturn(Optional.empty());

        Optional<User> result = service.getById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_allFieldsUpdated() {
        User existing = new User();
        existing.setId(1L);
        existing.setUserName("old");
        existing.setPassword("oldPass");

        User patch = new User();
        patch.setUserName("new");
        patch.setPassword("newPass");

        when(repo.getById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class), eq("ADMIN"))).thenAnswer(inv -> inv.getArgument(0));

        Optional<User> result = service.update(1L, patch, "ADMIN");

        assertTrue(result.isPresent());
        assertEquals("new", result.get().getUserName());
        assertEquals("newPass", result.get().getPassword());
        verify(repo).save(existing, "ADMIN");
    }

    @Test
    void updateUser_onlyUsernameUpdated() {
        User existing = new User();
        existing.setUserName("old");
        existing.setPassword("oldPass");

        User patch = new User();
        patch.setUserName("new");

        when(repo.getById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class), anyString())).thenReturn(existing);

        Optional<User> result = service.update(1L, patch, "USER");

        assertTrue(result.isPresent());
        assertEquals("new", existing.getUserName());
        assertEquals("oldPass", existing.getPassword());
    }

    @Test
    void updateUser_notFound() {
        when(repo.getById(1L)).thenReturn(Optional.empty());

        Optional<User> result = service.update(1L, new User(), "USER");

        assertTrue(result.isEmpty());
        verify(repo, never()).save(any(), any());
    }

    @Test
    void update_doesNotUpdateUsername_whenPatchUsernameIsNull() {
        User existing = new User();
        existing.setUserName("old");

        User patch = new User();

        when(repo.getById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class), anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        service.update(1L, patch, "USER");

        assertEquals("old", existing.getUserName());
    }

    @Test
    void deleteUser_ok() {
        when(repo.getById(1L)).thenReturn(Optional.of(new User()));

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repo).deleteById(1L);
    }

    @Test
    void deleteUser_notFound() {
        when(repo.getById(1L)).thenReturn(Optional.empty());

        boolean result = service.delete(1L);

        assertFalse(result);
        verify(repo, never()).deleteById(anyLong());
    }
}
