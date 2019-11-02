package com.vladimirkomlev.workoutdiary;

import com.vladimirkomlev.workoutdiary.config.ContainerProperties;
import com.vladimirkomlev.workoutdiary.config.LocalProperties;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;

@SpringBootConfiguration
@SpringBootApplication
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-local.properties"})
@EnableConfigurationProperties({ContainerProperties.class, LocalProperties.class})
public class WorkoutDiaryApplicationTests {
}
