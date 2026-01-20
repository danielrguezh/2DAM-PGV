package com.docencia.tasks.infrastructure.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.docencia.tasks.adapters.out.persistence.jpa.UserJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.UserJpaRepository;

/**
 * @author prorix
 * @version 1.0.0
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserJpaRepository repositorio;

    public JpaUserDetailsService(UserJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJpaEntity user = repositorio.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRol()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities);
    }

}
