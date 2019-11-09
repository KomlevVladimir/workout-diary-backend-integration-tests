package com.vladimirkomlev.workoutdiary.model;

public class AuthRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public AuthRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest withPassword(String password) {
        this.password = password;
        return this;
    }
}