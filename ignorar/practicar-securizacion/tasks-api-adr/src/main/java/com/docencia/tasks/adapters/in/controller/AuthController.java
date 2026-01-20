package com.docencia.tasks.adapters.in.controller;

import com.docencia.tasks.adapters.in.api.AuthRequest;
import com.docencia.tasks.adapters.in.api.AuthResponse;
import com.docencia.tasks.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth")
@CrossOrigin
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  @Operation(summary = "Login (devuelve JWT)")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    // 1. Autenticar usando el AuthenticationManager de Spring Security.
    // Esto comprueba usuario y contrasenia contra el UserDetailsService
    // configurado.
    // Si falla, lanza AuthenticationException (BadCredentials).
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    // 2. Si la autenticacion es exitosa, obtenemos el usuario.
    UserDetails user = (UserDetails) auth.getPrincipal();

    // 3. Generamos el token JWT firmado para este usuario.
    String token = jwtService.generateToken(user);

    List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    // 4. Devolvemos el token al cliente (quien debera guardarlo y enviarlo en
    // "Authorization: Bearer <token>")
    return ResponseEntity.ok(new AuthResponse(token, roles));
  }
}
