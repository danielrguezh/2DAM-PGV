package com.docencia.tasks.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

  private final SecretKey key;
  private final long expirationMinutes;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-minutes}") long expirationMinutes
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMinutes = expirationMinutes;
  }

  public String generateToken(String username) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expirationMinutes * 60);

    return Jwts.builder()
        .subject(username)
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public boolean isValid(String token) {
    try {
      Claims c = parseClaims(token);
      return c.getExpiration().after(new Date());
    } catch (Exception ex) {
      return false;
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}