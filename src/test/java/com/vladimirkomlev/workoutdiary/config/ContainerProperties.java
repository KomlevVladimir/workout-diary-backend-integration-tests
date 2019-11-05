package com.vladimirkomlev.workoutdiary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "container")
public class ContainerProperties {
    private String backendName;
    private int backendPort;
    private String dbName;
    private int dbPort;
    private String mailhogName;
    private int mailhogPort;
    private Duration environmentStartupTimeout;

    public String getBackendName() {
        return backendName;
    }

    public void setBackendName(String backendName) {
        this.backendName = backendName;
    }

    public int getBackendPort() {
        return backendPort;
    }

    public void setBackendPort(int backendPort) {
        this.backendPort = backendPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getDbPort() {
        return dbPort;
    }

    public String getMailhogName() {
        return mailhogName;
    }

    public void setMailhogName(String mailhogName) {
        this.mailhogName = mailhogName;
    }

    public int getMailhogPort() {
        return mailhogPort;
    }

    public void setMailhogPort(int mailhogPort) {
        this.mailhogPort = mailhogPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    public Duration getEnvironmentStartupTimeout() {
        return environmentStartupTimeout;
    }

    public void setEnvironmentStartupTimeout(Duration environmentStartupTimeout) {
        this.environmentStartupTimeout = environmentStartupTimeout;
    }
}
