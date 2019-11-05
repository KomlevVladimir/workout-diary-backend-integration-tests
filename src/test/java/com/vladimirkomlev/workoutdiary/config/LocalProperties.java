package com.vladimirkomlev.workoutdiary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Profile("local")
public class LocalProperties {
    private String backendUrl;
    private String dbUrl;
    private String mailhogUrl;

    public String getBackendUrl() {
        return backendUrl;
    }

    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getMailhogUrl() {
        return mailhogUrl;
    }

    public void setMailhogUrl(String mailHogUrl) {
        this.mailhogUrl = mailHogUrl;
    }
}
