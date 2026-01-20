package com.docencia.tasks.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter)
      throws Exception {
    http
        // Deshabilitamos CSRF (Cross-Site Request Forgery) ya que en APIs REST
        // stateless (sin sesion)
        // no es necesario y facilita las pruebas desde clientes como Postman o
        // frontends externos.
        .csrf(csrf -> csrf.disable())

        // Configuracion de headers, por ejemplo para permitir frames desde el mismo
        // origen (util para H2 console)
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

        // Gestion de sesiones: STATELESS indica que no se creara una HttpSession en el
        // servidor.
        // La autenticacion debe viajar en cada peticion (ej. via Token JWT).
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Reglas de autorizacion (RBAC - Role Based Access Control)
        .authorizeHttpRequests(auth -> auth
            // Endpoints publicos que no requieren autenticacion
            // auth, swagger, h2-console...
            .requestMatchers(
                "/api/v1/auth/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/h2-console/**")
            .permitAll()

            // Endpoints protegidos por ROL
            // GET tareas: pueden acceder tanto USER como ADMIN
            .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAnyRole("USER", "ADMIN")

            // Operaciones de escritura (POST, PATCH, DELETE) sobre tareas: solo ADMIN
            // Esto implementa el principio de menor privilegio.
            .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/tasks/**").hasRole("ADMIN")

            // Cualquier otra peticion requiere estar autenticado (al menos tener un token
            // valido)
            .anyRequest().authenticated())
        // Deshabilitamos Basic Auth porque usaremos JWT
        .httpBasic(httpBasic -> httpBasic.disable());

    // Aniadimos nuestro filtro JWT antes del filtro de autenticacion por defecto de
    // Username/Password.
    // Esto permite que si viene un token valido, se establezca la identidad antes
    // de llegar a los controladores.
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * Defines el servicio que carga los detalles del usuario.
   * En un entorno real, esto consultaria una Base de Datos.
   * Para este ejemplo (y examenes), usamos InMemoryUserDetailsManager con
   * usuarios hardcodeados.
   * 
   * Usuarios:
   * - admin / admin123 (Rol ADMIN)
   * - user / user123 (Rol USER)
   */
  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    return new InMemoryUserDetailsManager(
        User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build(),
        User.withUsername("user").password(encoder.encode("user123")).roles("USER").build());
  }

  /**
   * Bean para encriptar contrasenias.
   * BCrypt es un algoritmo de hashing seguro que incluye 'salt' automaticamente.
   * Nunca se deben guardar contrasenias en texto plano.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
