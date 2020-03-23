package com.vladimirkomlev.workoutdiary.model;

public class SetupPasswordRequest {
    private String code;
    private String password;

    public String getCode() {
        return code;
    }

    public SetupPasswordRequest withCode(String code) {
        this.code = code;
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