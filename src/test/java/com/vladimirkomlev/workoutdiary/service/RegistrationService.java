package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.UserCreateRequest;
import com.vladimirkomlev.workoutdiary.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationService {
    private final RestTemplate restTemplate;
    private final EnvironmentService environmentService;

    @Autowired
    public RegistrationService(RestTemplate restTemplate, EnvironmentService environmentService) {
        this.restTemplate = restTemplate;
        this.environmentService = environmentService;
    }

    public UserResponse signUp(UserCreateRequest request) {
        return restTemplate.postForObject(environmentService.backendUrl() + "/signup", request, UserResponse.class);
    }
}
