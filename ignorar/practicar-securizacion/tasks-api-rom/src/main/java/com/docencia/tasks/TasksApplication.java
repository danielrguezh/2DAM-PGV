package com.docencia.tasks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.docencia.tasks.adapters.out.persistence.jpa.RolJpaEntity;
import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.RolJpaRepository;
import com.docencia.tasks.adapters.out.persistence.repository.UserJpaRepository;

/**
 * Clase principal de la aplicacion
 * @author prorix
 * @version 1.0.0
 */
@SpringBootApplication
public class TasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasksApplication.class, args);
    }

    @Bean
    CommandLineRunner initRolesAndUsers(RolJpaRepository rolRepo, UserJpaRepository userRepo) {
        return args -> {
            RolJpaEntity adminRole = rolRepo.findByRol("ADMIN")
                    .orElseGet(() -> rolRepo.save(new RolJpaEntity(null, "ADMIN")));

            RolJpaEntity userRole = rolRepo.findByRol("USER")
                    .orElseGet(() -> rolRepo.save(new RolJpaEntity(null, "USER")));

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            userRepo.findByUserName("admin").orElseGet(() -> {
                UserJpaEntity admin = new UserJpaEntity();
                admin.setUserName("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.getRoles().add(adminRole);
                return userRepo.save(admin);
            });

            userRepo.findByUserName("user").orElseGet(() -> {
                UserJpaEntity user = new UserJpaEntity();
                user.setUserName("user");
                user.setPassword(encoder.encode("user123"));
                user.getRoles().add(userRole);
                return userRepo.save(user);
            });
        };
    }
}
