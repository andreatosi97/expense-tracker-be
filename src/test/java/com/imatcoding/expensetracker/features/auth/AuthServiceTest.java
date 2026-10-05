package com.imatcoding.expensetracker.features.auth;

import com.imatcoding.expensetracker.common.util.JwtUtil;
import com.imatcoding.expensetracker.features.auth.fixture.AuthFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationConfiguration authConfig;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        when(authConfig.getAuthenticationManager()).thenReturn(authManager);
    }

    @Test
    void login_authenticatesAndReturnsToken() {
        String username = AuthFixtures.loginIn().username();
        String password = AuthFixtures.loginIn().password();
        String expectedToken = "jwt-token";
        when(jwtUtil.generateToken(username)).thenReturn(expectedToken);

        String token = authService.login(username, password);

        assertThat(token).isEqualTo(expectedToken);
        verify(jwtUtil).generateToken(username);

        // AuthenticationManager argument checks
        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authManager).authenticate(captor.capture());
        Authentication authentication = captor.getValue();
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getPrincipal()).isEqualTo(username);
        assertThat(authentication.getCredentials()).isEqualTo(password);
    }

    @Test
    void login_notGenerateTokenWhenAuthenticationFails() {
        String username = AuthFixtures.loginIn().username();
        String password = AuthFixtures.loginIn().password();
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(ex);

        assertThatThrownBy(() -> authService.login(username, password))
                .isSameAs(ex);

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtUtil);
    }
}
