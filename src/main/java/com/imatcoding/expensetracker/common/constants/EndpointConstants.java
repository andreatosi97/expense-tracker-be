package com.imatcoding.expensetracker.common.constants;

public final class EndpointConstants {

    private EndpointConstants() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static final String BASE_ENDPOINT = "/api";

    public static final String AUTH_ENDPOINT = BASE_ENDPOINT + "/auth";
}
