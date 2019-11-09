package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.ResetPasswordRequest;
import com.vladimirkomlev.workoutdiary.model.SetupPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PasswordService {
    private final RestTemplate restTemplate;
    private final EnvironmentService environmentService;

    @Autowired
    public PasswordService(RestTemplate restTemplate, EnvironmentService environmentService) {
        this.restTemplate = restTemplate;
        this.environmentService = environmentService;
    }

    public void resetPassword(ResetPasswordRequest request) {
        restTemplate.postForEntity(environmentService.backendUrl() + "/reset-password", request, String.class);
    }

    public void setupPassword(SetupPasswordRequest request) {
        restTemplate.postForEntity(environmentService.backendUrl() + "/setup-password", request, String.class);
    }
}