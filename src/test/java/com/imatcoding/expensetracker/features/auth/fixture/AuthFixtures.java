package com.imatcoding.expensetracker.features.auth.fixture;

import com.imatcoding.expensetracker.features.auth.model.dto.LoginIn;

public final class AuthFixtures {

    private AuthFixtures() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static LoginIn loginIn() {
        return new LoginIn("test-user", "test-password");
    }

    public static LoginIn loginIn(String username, String password) {
        return new LoginIn(username, password);
    }
}
