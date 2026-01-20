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
      @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds) {
    // Decodificamos la clave secreta desde Base64. Esta clave firma el token.
    // Si alguien modifica el token, la firma no coincidira y sera rechazado.
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    this.expirationSeconds = expirationSeconds;
  }

  /**
   * Genera un nuevo token JWT para el usuario autenticado.
   * Incluye:
   * - Subject: username
   * - IssuedAt / Expiration: fechas de validez
   * - Claims personalizados: roles (para usarlos en el frontend o filtro)
   * - Firma: HMAC-SHA con nuestra clave secreta
   */
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

  /**
   * Valida el token:
   * 1. Verifica la firma (parseAllClaims lanza excepcion si falla).
   * 2. Verifica que el usuario del token coincida con el usuario real (opcional,
   * pero buena practica).
   * 3. Verifica que no haya expirado.
   */
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
        .parseSignedClaims(token) // Lanza excepciones si la firma es invalida o el token expiro.
        .getPayload();
  }
}
