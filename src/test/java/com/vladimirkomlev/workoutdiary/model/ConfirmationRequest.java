package com.vladimirkomlev.workoutdiary.model;

public class ConfirmationRequest {
    private String code;

    public String getCode() {
        return code;
    }

    public ConfirmationRequest withCode(String code) {
        this.code = code;
        return this;
    }
}
