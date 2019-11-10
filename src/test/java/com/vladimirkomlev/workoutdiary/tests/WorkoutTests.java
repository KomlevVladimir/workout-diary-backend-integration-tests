package com.vladimirkomlev.workoutdiary.tests;

import com.vladimirkomlev.workoutdiary.model.WorkoutCreateUpdateRequest;
import com.vladimirkomlev.workoutdiary.model.WorkoutResponse;
import com.vladimirkomlev.workoutdiary.service.WorkoutService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@SpringBootTest
public class WorkoutTests {
    private static final long REGISTERED_USER_ID = 25L;

    @Autowired
    private WorkoutService workoutService;

    @Test
    @DisplayName("Create workout with valid data")
    public void createWorkoutWithValidDataTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        Set<WorkoutResponse> workoutsBefore = workoutService.getAllWorkouts(REGISTERED_USER_ID);
        WorkoutResponse response = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);
        WorkoutResponse workout = workoutService.getWorkout(REGISTERED_USER_ID, response.getId());
        Set<WorkoutResponse> workoutsAfter = workoutService.getAllWorkouts(REGISTERED_USER_ID);
        workoutsBefore.add(workout);

        assertAll(
                () -> assertEquals(
                        workoutRequest.getTitle(),
                        response.getTitle(),
                        "The title in the request is " + workoutRequest.getTitle() +
                                " but in the response is " + response.getTitle()
                ),
                () -> assertEquals(
                        workoutRequest.getDate(),
                        response.getDate().toString(),
                        "The date in the request is " + workoutRequest.getDate() +
                                " but in the response is " + response.getDate()
                ),
                () -> assertEquals(
                        workoutRequest.getDescription(),
                        response.getDescription(),
                        "The description in the request is " + workoutRequest.getDescription() +
                                " but in the response is " + response.getDescription()
                ),
                () -> assertTrue(response.getId() > 0, "Workout id is not generated"),
                () -> assertEquals(workoutsBefore, workoutsAfter, "Wotkout is not created")
        );
    }

    @Test
    @DisplayName("Create workout with empty title")
    public void createWorkoutWithEmptyTitleTest() {
        WorkoutCreateUpdateRequest workoutRequest = new WorkoutCreateUpdateRequest()
                .withTitle("")
                .withDate(randomDate())
                .withDescription(randomDescription());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @Test
    @DisplayName("Create workout with empty date")
    public void createWorkoutWithEmptyDateTest() {
        WorkoutCreateUpdateRequest workoutRequest = new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate("")
                .withDescription(randomDescription());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @Test
    @DisplayName("Create workout with empty description")
    public void createWorkoutWithEmptyDescriptionTest() {
        WorkoutCreateUpdateRequest workoutRequest = new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate(randomDate())
                .withDescription("");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @Test
    @DisplayName("Create workout for another user")
    public void createWorkoutForAnotherUserTest() {
        long anotherUserId = randomLong();
        WorkoutCreateUpdateRequest workoutRequest = new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate(randomDate())
                .withDescription(randomDescription());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.createWorkout(anotherUserId, workoutRequest));

        assertEquals(FORBIDDEN, exception.getStatusCode(), "Status code is not " + FORBIDDEN);
    }

    @Test
    @DisplayName("Get all workouts")
    public void getAllWorkoutsForAnotherUserTest() {
        long anotherUserId = randomLong();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.getAllWorkouts(anotherUserId));

        assertEquals(FORBIDDEN, exception.getStatusCode(), "Status code is not " + FORBIDDEN);
    }

    @Test
    @DisplayName("Get workout")
    public void getWorkoutTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);
        WorkoutResponse response = workoutService.getWorkout(REGISTERED_USER_ID, createdWorkout.getId());

        assertEquals(createdWorkout, response, "Could not get workout");
    }

    @Test
    @DisplayName("Update workout with valid data")
    public void updateWorkoutTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        WorkoutCreateUpdateRequest workoutUpdateRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse response =
                workoutService.updateWorkout(REGISTERED_USER_ID, createdWorkout.getId(), workoutUpdateRequest);

        assertAll(
                () -> assertEquals(
                        workoutUpdateRequest.getTitle(),
                        response.getTitle(),
                        "The title in the request is " + workoutUpdateRequest.getTitle() +
                                " but in the response is " + response.getTitle()
                ),
                () -> assertEquals(
                        workoutUpdateRequest.getDate(),
                        response.getDate().toString(),
                        "The date in the request is " + workoutUpdateRequest.getDate() +
                                " but in the response is " + response.getDate()
                ),
                () -> assertEquals(
                        workoutUpdateRequest.getDescription(),
                        response.getDescription(),
                        "The description in the request is " + workoutUpdateRequest.getDescription() +
                                " but in the response is " + response.getDescription()
                )
        );

    }

    @Test
    @DisplayName("Update workout with empty title")
    public void updateWorkoutWithEmptyTitleTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        WorkoutCreateUpdateRequest workoutUpdateRequest = new WorkoutCreateUpdateRequest()
                .withTitle("")
                .withDate(randomDate())
                .withDescription(randomDescription());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.updateWorkout(REGISTERED_USER_ID, createdWorkout.getId(), workoutUpdateRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @Test
    @DisplayName("Update workout with empty date")
    public void updateWorkoutWithEmptyDateTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        WorkoutCreateUpdateRequest workoutUpdateRequest = new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate("")
                .withDescription(randomDescription());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.updateWorkout(REGISTERED_USER_ID, createdWorkout.getId(), workoutUpdateRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @Test
    @DisplayName("Update workout with empty description")
    public void updateWorkoutWithEmptyDescriptionTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        WorkoutCreateUpdateRequest workoutUpdateRequest = new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate(randomDate())
                .withDescription("");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.updateWorkout(REGISTERED_USER_ID, createdWorkout.getId(), workoutUpdateRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @Test
    @DisplayName("Update workout for another user")
    public void updateWorkoutForAnotherUserTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        long anotherUserId = randomLong();
        WorkoutCreateUpdateRequest workoutUpdateRequest = new WorkoutCreateUpdateRequest()
                .withTitle(randomTitle())
                .withDate(randomDate())
                .withDescription(randomDescription());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.updateWorkout(anotherUserId, createdWorkout.getId(), workoutUpdateRequest));

        assertEquals(FORBIDDEN, exception.getStatusCode(), "Status code is not " + FORBIDDEN);
    }

    @Test
    @DisplayName("Delete workout")
    public void deleteWorkoutTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        Set<WorkoutResponse> workoutsBefore = workoutService.getAllWorkouts(REGISTERED_USER_ID);
        workoutService.deleteWorkout(REGISTERED_USER_ID, createdWorkout.getId());
        Set<WorkoutResponse> workoutsAfter = workoutService.getAllWorkouts(REGISTERED_USER_ID);
        workoutsBefore.remove(createdWorkout);

        assertEquals(workoutsBefore, workoutsAfter, "The workout is not deleted");
    }

    @Test
    @DisplayName("Delete workout for another user")
    public void deleteWorkoutForAnotherUserTest() {
        WorkoutCreateUpdateRequest workoutRequest = randomWorkoutCreateUpdateRequest();
        WorkoutResponse createdWorkout = workoutService.createWorkout(REGISTERED_USER_ID, workoutRequest);

        long anotherUserId = randomLong();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                workoutService.deleteWorkout(anotherUserId, createdWorkout.getId()));

        assertEquals(FORBIDDEN, exception.getStatusCode(), "Status code is not " + FORBIDDEN);
    }
}