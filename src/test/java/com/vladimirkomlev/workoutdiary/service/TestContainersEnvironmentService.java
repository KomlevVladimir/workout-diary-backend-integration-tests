package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.config.ContainerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.DockerComposeContainer;

import javax.annotation.PreDestroy;
import java.io.File;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;

@Service
@Profile("default")
public class TestContainersEnvironmentService implements EnvironmentService {
    private final ContainerProperties containerProperties;
    private DockerComposeContainer environment = new DockerComposeContainer(new File("docker-compose.yml"));

    @Autowired
    public TestContainersEnvironmentService(ContainerProperties containerProperties) {
        this.containerProperties = containerProperties;
        startEnvironment();
    }

    private void startEnvironment() {
        environment
                .withExposedService(
                        containerProperties.getBackendName(),
                        containerProperties.getBackendPort(),
                        forListeningPort().withStartupTimeout(containerProperties.getEnvironmentStartupTimeout())
                )
                .withExposedService(
                        containerProperties.getDbName(),
                        containerProperties.getDbPort(),
                        forListeningPort().withStartupTimeout(containerProperties.getEnvironmentStartupTimeout())
                )
                .withPull(true).withLocalCompose(true)
                .start();
    }

    @PreDestroy
    private void stop() {
        environment.stop();
    }

    private String containerHost(String serviceName, int servicePort) {
        return environment.getServiceHost(serviceName, servicePort);
    }

    private int containerPort(String serviceName, int servicePort) {
        return environment.getServicePort(serviceName, servicePort);
    }

    private String getContainerUrl(String containerName, int containerPort) {
        String host = containerHost(containerName, containerPort);
        int port = containerPort(containerName, containerPort);
        return host + ":" + port;
    }

    @Override
    public String backendUrl() {
        return "http://" +
                getContainerUrl(containerProperties.getBackendName(), containerProperties.getBackendPort());
    }

    @Override
    public String dbUrl() {
        return "jdbc:postgresql://" +
                getContainerUrl(containerProperties.getDbName(), containerProperties.getDbPort()) +
                "/postgres";
    }
}
