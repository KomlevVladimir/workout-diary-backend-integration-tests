package com.vladimirkomlev.workoutdiary.model;

public class AuthResponse {
    private Long userId;
    private String token;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}