package com.docencia.tasks;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.RolJpaRepository;
import com.docencia.tasks.adapters.out.persistence.repository.UserJpaRepository;

@SpringBootTest
@Transactional
class TasksApplicationTest {

    @Autowired
    private RolJpaRepository rolRepo;

    @Autowired
    private UserJpaRepository userRepo;

    @Test
    void initRolesAndUsers_shouldCreateDefaultRolesAndUsers() {
        Optional<RolJpaEntity> adminRole = rolRepo.findByRol("ADMIN");
        Optional<RolJpaEntity> userRole = rolRepo.findByRol("USER");

        assertThat(adminRole).isPresent();
        assertThat(userRole).isPresent();

        Optional<UserJpaEntity> adminUser = userRepo.findByUserName("admin");
        Optional<UserJpaEntity> normalUser = userRepo.findByUserName("user");

        assertThat(adminUser).isPresent();
        assertThat(normalUser).isPresent();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertThat(encoder.matches("admin123", adminUser.get().getPassword())).isTrue();
        assertThat(encoder.matches("user123", normalUser.get().getPassword())).isTrue();

        assertThat(adminUser.get().getRoles()).extracting("rol").contains("ADMIN");
        assertThat(normalUser.get().getRoles()).extracting("rol").contains("USER");
    }
}