package com.vladimirkomlev.workoutdiary.model;

public class AuthResponse {
    private long userId;
    private String token;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}