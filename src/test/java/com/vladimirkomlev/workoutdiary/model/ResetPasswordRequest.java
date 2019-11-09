package com.vladimirkomlev.workoutdiary.model;

public class ResetPasswordRequest {
    private String email;

    public String getEmail() {
        return email;
    }

    public ResetPasswordRequest withEmail(String email) {
        this.email = email;
        return this;
    }
}