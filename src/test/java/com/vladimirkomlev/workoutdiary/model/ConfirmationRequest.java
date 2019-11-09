package com.vladimirkomlev.workoutdiary.model;

public class ConfirmationRequest {
    private String secret;

    public String getSecret() {
        return secret;
    }

    public ConfirmationRequest withSecret(String secret) {
        this.secret = secret;
        return this;
    }
}
