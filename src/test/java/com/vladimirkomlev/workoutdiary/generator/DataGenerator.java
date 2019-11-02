package com.vladimirkomlev.workoutdiary.generator;

import com.github.javafaker.Faker;
import com.vladimirkomlev.workoutdiary.model.UserCreateRequest;

import static java.lang.Long.*;

public class DataGenerator {
    private static Faker faker = new Faker();

    private static long randomLong() {
        return faker.number().numberBetween(MIN_VALUE, MAX_VALUE);
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
}
