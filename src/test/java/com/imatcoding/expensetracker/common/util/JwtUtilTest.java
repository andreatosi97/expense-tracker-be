package com.imatcoding.expensetracker.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "test-secret-key-must-be-long-enough-256-bits");
        jwtUtil.init();
    }

    @Test
    void generateToken_shouldContainUsername() {
        String token = jwtUtil.generateToken("username-test");
        assertEquals("username-test", jwtUtil.extractUsername(token));
    }

    @Test
    void isValid_shouldReturnTrueForFreshToken() {
        String token = jwtUtil.generateToken("username-test");
        assertTrue(jwtUtil.isValid(token));
    }

    @Test
    void isValid_shouldReturnFalseForMalformedToken() {
        assertFalse(jwtUtil.isValid("not.a.valid.token"));
    }

    @Test
    void isValid_shouldReturnFalseForExpiredToken() {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZS10ZXN0IiwiaWF0IjoxNzkwMjYyNDg4LCJleHAiOjE3OTAyNjI3ODh9.GAtS3cngc5y8KQxP5y6Q1ev_WtKkxcAEwnmyeSeygLY";
        assertFalse(jwtUtil.isValid(expiredToken));
    }

    @Test
    void isValid_shouldReturnFalseForTamperedToken() {
        String token = jwtUtil.generateToken("username-test");
        String tampered = token.substring(0, token.length() - 5) + "xxxxx";
        assertFalse(jwtUtil.isValid(tampered));
    }
}