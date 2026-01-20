package com.docencia.tasks.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 1. Obtener el header "Authorization"
    String authHeader = request.getHeader("Authorization");

    // Si no hay header o no empieza por "Bearer ", dejamos pasar la peticion
    // (si el endpoint requiere auth, fallara mas adelante en la cadena de
    // seguridad).
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // 2. Extraer el token (quitando "Bearer ")
    String token = authHeader.substring("Bearer ".length()).trim();

    // Verificamos que no estemos ya autenticados en el contexto (para no repetir
    // trabajo)
    if (token.isEmpty() || SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // 3. Extraer usuario y validar token
      String username = jwtService.extractUsername(token);
      if (username == null || username.isBlank()) {
        filterChain.doFilter(request, response);
        return;
      }

      // Cargamos los detalles del usuario (roles, etc)
      UserDetails user = userDetailsService.loadUserByUsername(username);

      // Si el token es valido (firma correcta y no expirado) para este usuario...
      if (jwtService.isTokenValid(token, user)) {
        // ...creamos un objeto Authentication y lo ponemos en el SecurityContext.
        // Esto indica a Spring Security que el usuario actual esta "logueado".
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (Exception ignored) {
      // Si el token es invalido, manipulado o expirado, simplemente no autenticamos.
      // El filtro de seguridad posterior rechazara la peticion si necesita auth.
      // Invalid token -> proceed without authentication (will be blocked by security
      // rules)
    }

    // Continuamos con el siguiente filtro de la cadena
    filterChain.doFilter(request, response);
  }
}
