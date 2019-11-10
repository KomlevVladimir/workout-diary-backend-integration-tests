package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.AuthRequest;
import com.vladimirkomlev.workoutdiary.model.WorkoutCreateUpdateRequest;
import com.vladimirkomlev.workoutdiary.model.WorkoutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.ClientHttpRequestFactorySupplier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.springframework.http.HttpMethod.*;

@Service
public class WorkoutService {
    private final EnvironmentService environmentService;
    private final AuthService authService;

    private RestTemplate restTemplate;

    @Autowired
    public WorkoutService(EnvironmentService environmentService, AuthService authService) {
        this.environmentService = environmentService;
        this.authService = authService;
        this.restTemplate = getAuthRestTemplate();
    }

    private RestTemplate getAuthRestTemplate() {
        AuthRequest authRequest = new AuthRequest().withEmail("test@myemail.com").withPassword("Password!1");
        String token = authService.signIn(authRequest).getToken();
        return new RestTemplateBuilder().requestFactory(new ClientHttpRequestFactorySupplier())
                .interceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                }).build();
    }

    public WorkoutResponse createWorkout(long userId, WorkoutCreateUpdateRequest request) {
        return restTemplate.postForObject(
                environmentService.backendUrl() + "/users/" + userId + "/workouts",
                request,
                WorkoutResponse.class
        );
    }

    public Set<WorkoutResponse> getAllWorkouts(long userId) {
        return restTemplate.exchange(
                environmentService.backendUrl() + "/users/" + userId + "/workouts",
                GET,
                null,
                new ParameterizedTypeReference<Set<WorkoutResponse>>() {}
                ).getBody();
    }

    public WorkoutResponse getWorkout(long userId, long workoutId) {
        return restTemplate.getForObject(
                environmentService.backendUrl() + "/users/" + userId + "/workouts/" + workoutId,
                WorkoutResponse.class
        );
    }

    public WorkoutResponse updateWorkout(long userId, long workoutId, WorkoutCreateUpdateRequest request) {
        HttpEntity<WorkoutCreateUpdateRequest> entity = new HttpEntity<>(request);
        return restTemplate.exchange(
                environmentService.backendUrl() + "/users/" + userId + "/workouts/" + workoutId,
                PUT,
                entity,
                WorkoutResponse.class
        ).getBody();
    }

    public void deleteWorkout(long userId, long workoutId) {
        restTemplate.delete(environmentService.backendUrl() + "/users/" + userId + "/workouts/" + workoutId);
    }
}
