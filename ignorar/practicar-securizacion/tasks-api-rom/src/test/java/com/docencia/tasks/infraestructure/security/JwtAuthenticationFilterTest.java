package com.docencia.tasks.infraestructure.security;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.docencia.tasks.infrastructure.security.JwtAuthenticationFilter;
import com.docencia.tasks.infrastructure.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

class JwtAuthenticationFilterTest {

    JwtService jwtService;
    UserDetailsService userDetailsService;
    JwtAuthenticationFilter filter;

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_noAuthorizationHeader_shouldCallChainWithoutAuth() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_invalidTokenFormat_shouldCallChainWithoutAuth() throws ServletException, IOException {
        request.addHeader("Authorization", "InvalidToken");
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_validTokenAndUsernameMatch_shouldSetAuthentication() throws ServletException, IOException {
        String token = "valid-token";
        request.addHeader("Authorization", "Bearer " + token);

        UserDetails user = User.withUsername("usuario").password("pass").roles("USER").build();

        when(jwtService.extractUsername(token)).thenReturn("usuario");
        when(userDetailsService.loadUserByUsername("usuario")).thenReturn(user);
        when(jwtService.isValid(token)).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("usuario", SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(
                SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
    }

    @Test
    void doFilter_validToken_butUsernameMismatch_shouldNotSetAuthentication() throws ServletException, IOException {
        String token = "valid-token";
        request.addHeader("Authorization", "Bearer " + token);

        UserDetails user = User.withUsername("someoneElse").password("pass").roles("USER").build();

        when(jwtService.extractUsername(token)).thenReturn("usuario");
        when(userDetailsService.loadUserByUsername("usuario")).thenReturn(user);
        when(jwtService.isValid(token)).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_invalidToken_shouldNotSetAuthentication() throws ServletException, IOException {
        String token = "invalid-token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.extractUsername(token)).thenReturn("usuario");
        when(userDetailsService.loadUserByUsername("usuario"))
                .thenReturn(User.withUsername("usuario").password("pass").roles("USER").build());
        when(jwtService.isValid(token)).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_usernameNull_shouldNotSetAuthentication() throws ServletException, IOException {
        String token = "token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.extractUsername(token)).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_usernameBlank_shouldNotSetAuthentication() throws ServletException, IOException {
        String token = "token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.extractUsername(token)).thenReturn("  ");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_authenticationAlreadySet_shouldNotOverwrite() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("someone", null, null));

        String token = "any-token";
        request.addHeader("Authorization", "Bearer " + token);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals("someone", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilter_emptyToken_shouldCallChainWithoutAuth() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer ");
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_authAlreadySet_shouldNotOverwrite() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("someone", null, null));
        request.addHeader("Authorization", "Bearer valid-token");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals("someone", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilter_exceptionInService_shouldCallChainWithoutAuth() throws ServletException, IOException {
        String token = "token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("fail"));

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
