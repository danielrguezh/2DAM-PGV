package com.docencia.tasks.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

  private final SecretKey key;
  private final long expirationSeconds;

  public JwtService(
      @Value("${app.jwt.secret}") String base64Secret,
      @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds
  ) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(UserDetails user) {
    Instant now = Instant.now();
    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return Jwts.builder()
        .subject(user.getUsername())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(expirationSeconds)))
        .claim("roles", roles)
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return parseAllClaims(token).getSubject();
  }

  public boolean isTokenValid(String token, UserDetails user) {
    Claims claims = parseAllClaims(token);
    String username = claims.getSubject();
    Date exp = claims.getExpiration();
    return username != null
        && username.equals(user.getUsername())
        && exp != null
        && exp.after(new Date());
  }

  private Claims parseAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
