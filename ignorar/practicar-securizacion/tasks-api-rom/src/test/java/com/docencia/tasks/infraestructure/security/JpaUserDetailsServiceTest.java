package com.docencia.tasks.infraestructure.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.UserJpaRepository;
import com.docencia.tasks.infrastructure.security.JpaUserDetailsService;



class JpaUserDetailsServiceTest {

    UserJpaRepository userRepo;
    JpaUserDetailsService service;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserJpaRepository.class);
        service = new JpaUserDetailsService(userRepo);
    }

    @Test
    void loadUserByUsername_existingUser_shouldReturnUserDetails() {
        UserJpaEntity user = new UserJpaEntity();
        user.setUserName("usuario");
        user.setPassword("hashed-pass");

        RolJpaEntity rol = new RolJpaEntity();
        rol.setRol("USER");
        user.setRoles(new HashSet<>(Collections.singletonList(rol)));

        when(userRepo.findByUserName("usuario")).thenReturn(Optional.of(user));

        
        UserDetails userDetails = service.loadUserByUsername("usuario");

        
        assertNotNull(userDetails);
        assertEquals("usuario", userDetails.getUsername());
        assertEquals("hashed-pass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_nonExistingUser_shouldThrowException() {
        when(userRepo.findByUserName("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown"));
    }
}