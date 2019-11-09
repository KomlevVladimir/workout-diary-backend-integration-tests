package com.vladimirkomlev.workoutdiary.model;

public class SetupPasswordRequest {
    private String secret;
    private String password;

    public String getSecret() {
        return secret;
    }

    public SetupPasswordRequest withSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SetupPasswordRequest withPassword(String password) {
        this.password = password;
        return this;
    }
}