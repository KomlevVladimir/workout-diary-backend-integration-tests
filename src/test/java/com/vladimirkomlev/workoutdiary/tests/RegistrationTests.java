package com.vladimirkomlev.workoutdiary.tests;

import com.vladimirkomlev.workoutdiary.model.AllMessagesResponse;
import com.vladimirkomlev.workoutdiary.model.UserCreateRequest;
import com.vladimirkomlev.workoutdiary.model.UserResponse;
import com.vladimirkomlev.workoutdiary.service.MailService;
import com.vladimirkomlev.workoutdiary.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
public class RegistrationTests {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MailService mailService;

    @Test
    @DisplayName("Register user with valid data")
    public void registerUserWithValidDataTest() {
        UserCreateRequest request = randomUserCreateRequest();
        AllMessagesResponse messagesBefore = mailService.getMessages(1000);
        UserResponse response = registrationService.signUp(request);
        mailService.waitAndReceiveMessage();
        AllMessagesResponse messagesAfter = mailService.getMessages(1000);

        assertAll(
                () -> assertEquals(
                        request.getFirstName(),
                        response.getFirstName(),
                        "The firstname in the request is " + request.getFirstName() +
                                " but in the response is " + response.getFirstName()
                ),
                () -> assertEquals(
                        request.getLastName(),
                        response.getLastName(),
                        "The lastname in the request is " + request.getLastName() +
                                " but in ther response is " + response.getLastName()
                ),
                () -> assertEquals(
                        request.getAge(),
                        response.getAge(),
                        "The age in the request is " + request.getAge() +
                                " but in the response is " + response.getAge()
                ),
                () -> assertEquals(
                        request.getEmail(),
                        response.getEmail(),
                        "The email in the request is " + request.getEmail() +
                                " but in the response is " + response.getEmail()
                ),
                () -> assertTrue(response.getId() > 0, "User id is not generated"),
                () -> assertEquals(
                        messagesBefore.getCount(),
                        messagesAfter.getCount() - 1,
                        "Email confirmation message is not received"
                )
        );
    }

    @Test
    @DisplayName("Register user with empty firstname")
    public void registerUserWithEmptyFirstNameTest() {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName("")
                .withLastName(randomLastName())
                .withAge(randomAge())
                .withEmail(randomEmail())
                .withPassword(randomPassword());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("firstName: should not be blank"),
                        exception.getResponseBodyAsString() + " does not contain 'firstName: should not be blank'"
                )
        );
    }

    @Test
    @DisplayName("Register user with empty lastname")
    public void registerUserWithEmptyLastNameTest() {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName("")
                .withAge(randomAge())
                .withEmail(randomEmail())
                .withPassword(randomPassword());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("lastName: should not be blank"),
                        exception.getResponseBodyAsString() + " does not contain 'lastName: should not be blank'"
                )
        );
    }

    @Test
    @DisplayName("Register user with age more than 3 digits")
    public void registerUserWithAgeMoreThanThreeDigitsTest() {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAge(1000)
                .withEmail(randomEmail())
                .withPassword(randomPassword());

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("age: no more than 3 digits"),
                        exception.getResponseBodyAsString() + " does not contain 'age: no more than 3 digits'"
                )
        );
    }

    @Test
    @DisplayName("Register user with empty email")
    public void registerUserWithEmptyEmailTest() {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAge(randomAge())
                .withEmail("")
                .withPassword(randomPassword());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("email: should not be blank"),
                        exception.getResponseBodyAsString() + " does not contain 'email: should not be blank'"
                )
        );
    }

    @Test
    @DisplayName("Register user with invalid email")
    public void registerUserWithInvalidEmailTest() {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAge(randomAge())
                .withEmail("test")
                .withPassword(randomPassword());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("email: must have valid email format"),
                        exception.getResponseBodyAsString() + " does not contain 'email: must have valid email format'"
                )
        );
    }

    @Test
    @DisplayName("Register user with empty password")
    public void registerUserWithEmptyPasswordTest() {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAge(randomAge())
                .withEmail(randomEmail())
                .withPassword("");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("password: should not be blank"),
                        exception.getResponseBodyAsString() + " does not contain 'password: should not be blank'"
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Register user with invalid password")
    @ValueSource(strings = {"test", "test@123", "T@123", "Test!test", "Test@!$%^"})
    public void registerUserWithInvalidPasswordTest(String password) {
        UserCreateRequest request = new UserCreateRequest()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAge(randomAge())
                .withEmail(randomEmail())
                .withPassword(password);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                registrationService.signUp(request));

        assertAll(
                () -> assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST),
                () -> assertTrue(
                        exception.getResponseBodyAsString().contains("must have at least 8 characters and include" +
                                " uppercase and lowercase letters and numbers"),
                        exception.getResponseBodyAsString() + " does not contain 'must have at least" +
                                " 8 characters and include uppercase and lowercase letters and numbers'"
                )
        );
    }
}
