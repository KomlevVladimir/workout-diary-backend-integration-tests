package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.config.LocalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class LocalEnvironmentService implements EnvironmentService {
    private final LocalProperties localProperties;

    @Autowired
    public LocalEnvironmentService(LocalProperties localProperties) {
        this.localProperties = localProperties;
    }

    @Override
    public String backendUrl() {
        return localProperties.getBackendUrl();
    }

    @Override
    public String mailhogUrl() {
        return localProperties.getMailhogUrl();
    }
}
