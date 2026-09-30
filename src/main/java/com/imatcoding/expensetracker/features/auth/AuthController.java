package com.imatcoding.expensetracker.features.auth;

import com.imatcoding.expensetracker.common.constants.EndpointConstants;
import com.imatcoding.expensetracker.common.model.ApiResult;
import com.imatcoding.expensetracker.features.auth.model.dto.LoginIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndpointConstants.AUTH_ENDPOINT)
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticate user and return JWT token
     */
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> login(@Valid @RequestBody LoginIn input) {
        String token = authService.login(input.username(), input.password());
        return new ApiResult<>(token);
    }
}
