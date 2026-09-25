package com.imatcoding.expensetracker.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION_MINUTES = 5;
    private static final long EXPIRATION_MILLIS = 1000 * 60 * EXPIRATION_MINUTES;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token)
                .getPayload().getSubject();
    }

    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Reusable token parsing
    private Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token);
    }
}