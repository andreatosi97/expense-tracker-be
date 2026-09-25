package com.imatcoding.expensetracker.common.config;

import com.imatcoding.expensetracker.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    JwtUtil jwtUtil;
    @InjectMocks
    JwtFilter jwtFilter;

    @Mock
    FilterChain chain;

    MockHttpServletRequest req;
    MockHttpServletResponse res;

    @BeforeEach
    void setUp() {
        req = new MockHttpServletRequest();
        res = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_noHeader() throws Exception {
        jwtFilter.doFilterInternal(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void doFilterInternal_noBearerPrefix() throws Exception {
        req.addHeader("Authorization", "Basic abc123");

        jwtFilter.doFilterInternal(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void doFilterInternal_invalidToken() throws Exception {
        req.addHeader("Authorization", "Bearer bad.token");
        when(jwtUtil.isValid("bad.token")).thenReturn(false);

        jwtFilter.doFilterInternal(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
    }

    @Test
    void validToken_setsAuthentication() throws Exception {
        req.addHeader("Authorization", "Bearer valid.token.here");
        when(jwtUtil.isValid("valid.token.here")).thenReturn(true);
        when(jwtUtil.extractUsername("valid.token.here")).thenReturn("username-test");

        jwtFilter.doFilterInternal(req, res, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("username-test", auth.getName());
        verify(chain).doFilter(req, res);
    }
}
