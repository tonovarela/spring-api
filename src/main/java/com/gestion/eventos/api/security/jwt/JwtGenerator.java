package com.gestion.eventos.api.security.jwt;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;


import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
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

      return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith( getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (MalformedJwtException e){
            System.out.println("Token mal formado: " + e.getMessage());
        }
        catch (ExpiredJwtException e){
            System.out.println("Token expirado: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println("Token vacío: " + e.getMessage());
        }
        catch(UnsupportedJwtException e){
            System.out.println("Token desconocido: " + e.getMessage());
        }
        return false;
    }

}
