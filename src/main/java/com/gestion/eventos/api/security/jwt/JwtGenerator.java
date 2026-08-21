package com.gestion.eventos.api.security.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;


import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);
        log.debug("generateToken() - generando token para username={}, expira={}", username, expireDate);
        return Jwts.builder()
                .subject(username)
                .notBefore(currentDate)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();

    }

    public String getUsernameFromToken(String token) {
      Claims claims = Jwts.parser()
              .verifyWith(getSigningKey())
              .build()
              .parseSignedClaims(token)
              .getPayload();

      log.debug("getUsernameFromToken() - subject={}", claims.getSubject());
      return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith( getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            log.debug("validateToken() - token válido");
            return true;
        }catch (MalformedJwtException e){
            log.warn("Token mal formado: {}", e.getMessage());
        }
        catch (ExpiredJwtException e){
            log.debug("Token expirado: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            log.debug("Token vacío: {}", e.getMessage());
        }
        catch(UnsupportedJwtException e){
            log.warn("Token desconocido: {}", e.getMessage());
        }
        return false;
    }

}
