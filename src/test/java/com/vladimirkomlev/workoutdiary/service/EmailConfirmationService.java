package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.ConfirmationRequest;
import com.vladimirkomlev.workoutdiary.model.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailConfirmationService {
    private final RestTemplate restTemplate;
    private final EnvironmentService environmentService;

    public EmailConfirmationService(RestTemplate restTemplate, EnvironmentService environmentService) {
        this.restTemplate = restTemplate;
        this.environmentService = environmentService;
    }

    public UserResponse confirm(ConfirmationRequest request) {
        return restTemplate.postForObject(environmentService.backendUrl() + "/confirm", request, UserResponse.class);
    }
}
