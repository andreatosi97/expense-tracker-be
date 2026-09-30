package com.imatcoding.expensetracker.common.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    // FIXTURES

    record Body(@NotBlank(message = "must not be blank") String name) {
    }

    @RestController
    @RequestMapping("/test")
    static class ThrowingFixtureController {
        @PostMapping("/invalid-request")
        void validate(@Valid @RequestBody Body body) { /* fixture */ }

        @GetMapping("/auth-exception")
        void auth() {
            throw new BadCredentialsException("BadCredentialsException msg");
        }
    }

    // SETUP

    // Lighter than @WebMvcTest, no Spring Boot context, no Spring Security filters
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ThrowingFixtureController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    // TESTS

    @Test
    void handleValidation_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/test/invalid-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0].text").value("must not be blank"))
                .andExpect(jsonPath("$.messages[0].relatedFields[0]").value("name"));
    }

    @Test
    void handleAuthentication_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/test/auth-exception"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messages[0].text").value("BadCredentialsException msg"));
    }
}
