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
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    UserDetails user = (UserDetails) auth.getPrincipal();
    String token = jwtService.generateToken(user);
    List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    return ResponseEntity.ok(new AuthResponse(token, roles));
  }
}
