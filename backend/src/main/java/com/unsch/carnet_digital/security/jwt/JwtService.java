package com.unsch.carnet_digital.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXPIRATION = 1000L * 60 * 10;
    private final Key key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret debe tener al menos 32 caracteres");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    // 🔥 MÉTODO PRINCIPAL (ESTÁNDAR)
    public String generateToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email) // 👈 estándar: username/email
                .claim("rol", rol) // 👈 claim personalizado
                .setIssuer("carnet-digital")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    // 🔹 EXTRAER EMAIL
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 🔹 EXTRAER ROL
    public String extractRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // 🔹 EXTRAER CLAIMS
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer("carnet-digital")
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔹 VALIDAR TOKEN
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}