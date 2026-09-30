package com.imatcoding.expensetracker.features.auth;

import com.imatcoding.expensetracker.common.config.JwtFilter;
import com.imatcoding.expensetracker.common.config.SecurityConfig;
import com.imatcoding.expensetracker.common.constants.EndpointConstants;
import com.imatcoding.expensetracker.features.auth.fixture.AuthFixtures;
import com.imatcoding.expensetracker.features.auth.model.dto.LoginIn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Load only the AuthController and related MVC components = web slice (= partial
// Spring context: web layer beans like @ControllerAdvice, Jackson config, Filter,
// etc. no @Service, @Repository, @Component, etc.)
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtFilter.class, SecurityConfig.class}))
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JsonMapper jsonMapper;

    @MockitoBean
    AuthService authService;

    private static final String LOGIN_URL = EndpointConstants.AUTH_ENDPOINT + "/login";

    @Test
    void login_returnsTokenWhenServiceOk() throws Exception {
        LoginIn input = AuthFixtures.loginIn();
        when(authService.login(input.username(), input.password())).thenReturn("jwt-token");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("jwt-token"));

        verify(authService).login(input.username(), input.password());
    }

    @Test
    void login_returns400WhenBlankUsername() throws Exception {
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(AuthFixtures.loginIn("", "test-password"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void login_returns400WhenBlankPassword() throws Exception {
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(AuthFixtures.loginIn("test-user", ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
}
