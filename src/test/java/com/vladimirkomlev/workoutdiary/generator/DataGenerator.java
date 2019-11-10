package com.vladimirkomlev.workoutdiary.generator;

import com.github.javafaker.Faker;
import com.vladimirkomlev.workoutdiary.model.UserCreateRequest;
import com.vladimirkomlev.workoutdiary.model.WorkoutCreateUpdateRequest;

import java.time.format.DateTimeFormatter;

import static java.lang.Long.*;
import static java.time.LocalDate.now;

public class DataGenerator {
    private static Faker faker = new Faker();
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final int START_DATE_DAYS_RANGE = 0;
    private static final int END__DATE_DAYS_RANGE = 2000;

    public static long randomLong() {
        return faker.number().numberBetween(MIN_VALUE, MAX_VALUE);
    }

    public static String randomTitle() {
        return faker.esports().event();
    }

    public static String randomDescription() {
        return faker.esports().game();
    }

    public static String randomDate() {
        return now().minusDays(faker.number().numberBetween(START_DATE_DAYS_RANGE, END__DATE_DAYS_RANGE)).format(dateTimeFormatter);
    }

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }

    public static String randomEmail() {
        return "test@" + randomLong() + ".com";
    }

    public static String randomPassword() {
        return faker.name().firstName() + "@" + randomLong();
    }

    public static int randomAge() {
        return faker.number().numberBetween(3, 100);
    }

    public static UserCreateRequest randomUserCreateRequest() {
        return new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAge(randomAge())
                .withEmail(randomEmail())
                .withPassword(randomPassword());
    }

    public static WorkoutCreateUpdateRequest randomWorkoutCreateUpdateRequest() {
        return new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate(randomDate())
                .withDescription(randomDescription());
    }
}
