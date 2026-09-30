package com.imatcoding.expensetracker.features.auth.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginIn(
        @NotBlank(message = "Username required")
        String username,

        @NotBlank(message = "Password required")
        String password
) {
}
