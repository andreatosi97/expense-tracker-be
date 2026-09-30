package com.imatcoding.expensetracker.features.auth;

import com.imatcoding.expensetracker.common.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationConfiguration authConfig;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationConfiguration authConfig, JwtUtil jwtUtil) {
        this.authConfig = authConfig;
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        authConfig.getAuthenticationManager().authenticate(auth);

        return jwtUtil.generateToken(username);
    }
}
