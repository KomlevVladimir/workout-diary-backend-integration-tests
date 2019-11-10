package com.vladimirkomlev.workoutdiary.tests;

import com.vladimirkomlev.workoutdiary.model.*;
import com.vladimirkomlev.workoutdiary.service.AuthService;
import com.vladimirkomlev.workoutdiary.service.EmailConfirmationService;
import com.vladimirkomlev.workoutdiary.service.MailService;
import com.vladimirkomlev.workoutdiary.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomEmail;
import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomPassword;
import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomUserCreateRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest
public class LoginTests {
    private String email;
    private String password;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MailService mailService;

    @Autowired
    private EmailConfirmationService emailConfirmationService;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void registerUser() {
        UserCreateRequest userCreateRequest = randomUserCreateRequest();
        registrationService.signUp(userCreateRequest);
        String secret = mailService.getConfirmationSecretFromEmailConfirmationMessage();
        ConfirmationRequest confirmationRequest = new ConfirmationRequest().withSecret(secret);
        emailConfirmationService.confirm(confirmationRequest);
        email = userCreateRequest.getEmail();
        password = userCreateRequest.getPassword();
    }

    @Test
    @DisplayName("Login with valid data")
    public void loginWithValidDataTest() {
        AuthRequest authRequest = new AuthRequest().withEmail(email).withPassword(password);
        AuthResponse response = authService.signIn(authRequest);

        assertFalse(response.getToken().isEmpty(), "User is not logged in");
    }

    @Test
    @DisplayName("Login with invalid email")
    public void loginWithInvalidEmailTest() {
        String invalidEmail = randomEmail();
        AuthRequest authRequest = new AuthRequest().withEmail(invalidEmail).withPassword(password);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                authService.signIn(authRequest));

        assertEquals(UNAUTHORIZED, exception.getStatusCode(), "Status code is not " + UNAUTHORIZED);
    }

    @Test
    @DisplayName("Login with invalid password")
    public void loginWithInvalidPasswordTest() {
        String invalidPassword = randomPassword();
        AuthRequest authRequest = new AuthRequest().withEmail(email).withPassword(invalidPassword);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                authService.signIn(authRequest));

        assertEquals(UNAUTHORIZED, exception.getStatusCode(), "Status code is not " + UNAUTHORIZED);
    }

    @Test
    @DisplayName("Login with empty email")
    public void loginWithEmptyEmailTest() {
        AuthRequest authRequest = new AuthRequest().withEmail("").withPassword(password);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                authService.signIn(authRequest));

        assertEquals(UNAUTHORIZED, exception.getStatusCode(), "Status code is not " + UNAUTHORIZED);
    }

    @Test
    @DisplayName("Login with empty password")
    public void loginWithEmptyPasswordTest() {
        AuthRequest authRequest = new AuthRequest().withEmail(email).withPassword("");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                authService.signIn(authRequest));

        assertEquals(UNAUTHORIZED, exception.getStatusCode(), "Status code is not " + UNAUTHORIZED);
    }
}