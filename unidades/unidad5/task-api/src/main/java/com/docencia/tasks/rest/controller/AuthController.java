package com.docencia.tasks.rest.controller;

import com.docencia.tasks.infrastructure.security.JwtService;
import com.docencia.tasks.rest.dto.*;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String token = jwtService.generateToken(request.username());
        return new TokenResponse(token);
    }
}