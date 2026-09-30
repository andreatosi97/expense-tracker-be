package com.imatcoding.expensetracker.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;

@Component
public class JwtUtil {

    private static final long EXPIRATION_MINUTES = 5;
    private static final Duration EXPIRATION = Duration.ofMinutes(EXPIRATION_MINUTES);

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claims()
                    .add(Claims.ISSUED_AT, now.getEpochSecond())
                    .add(Claims.EXPIRATION, now.plus(EXPIRATION).getEpochSecond())
                    .and()
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