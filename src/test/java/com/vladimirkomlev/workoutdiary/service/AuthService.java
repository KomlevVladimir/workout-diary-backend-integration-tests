package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.AuthRequest;
import com.vladimirkomlev.workoutdiary.model.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final RestTemplate restTemplate;
    private final EnvironmentService environmentService;

    @Autowired
    public AuthService(RestTemplate restTemplate, EnvironmentService environmentService) {
        this.restTemplate = restTemplate;
        this.environmentService = environmentService;
    }

    public AuthResponse signIn(AuthRequest request) {
        return restTemplate.postForObject(environmentService.backendUrl() + "/token", request, AuthResponse.class);
    }
}
