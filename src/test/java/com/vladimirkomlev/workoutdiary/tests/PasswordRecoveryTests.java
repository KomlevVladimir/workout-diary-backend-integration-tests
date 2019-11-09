package com.vladimirkomlev.workoutdiary.tests;

import com.vladimirkomlev.workoutdiary.model.*;
import com.vladimirkomlev.workoutdiary.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomEmail;
import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomPassword;
import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomUserCreateRequest;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest
public class PasswordRecoveryTests {
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

    @Autowired
    private PasswordService passwordService;

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
    @DisplayName("Reset password")
    public void resetPasswordTest() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest().withEmail(email);
        AllMessagesResponse messagesBefore = mailService.getMessages(1000);
        passwordService.resetPassword(resetPasswordRequest);
        mailService.waitAndReceiveMessage();
        AllMessagesResponse messagesAfter = mailService.getMessages(1000);

        assertEquals(
                messagesBefore.getCount(),
                messagesAfter.getCount() - 1,
                "Reset password confirmation message is not received"
        );
    }

    @Test
    @DisplayName("Reset password with unregistered email")
    public void resetPasswordWithUnregisteredEmailTest() {
        String unregisteredEmail = randomEmail();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest().withEmail(unregisteredEmail);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                passwordService.resetPassword(resetPasswordRequest));

        assertEquals(NOT_FOUND, exception.getStatusCode(), "Status code is not " + NOT_FOUND);
    }

    @Test
    @DisplayName("Setup password with valid data")
    public void setupPasswordWithValidDataTest() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest().withEmail(email);
        passwordService.resetPassword(resetPasswordRequest);

        String secret = mailService.getSecretFromResetPasswordConfirmationMessage();
        String newPassword = randomPassword();
        SetupPasswordRequest setupPasswordRequest = new SetupPasswordRequest().withPassword(newPassword).withSecret(secret);
        passwordService.setupPassword(setupPasswordRequest);

        AuthRequest oldAuthRequest = new AuthRequest().withEmail(email).withPassword(password);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                authService.signIn(oldAuthRequest));

        AuthRequest newAuthRequest = new AuthRequest().withEmail(email).withPassword(newPassword);
        AuthResponse authResponse = authService.signIn(newAuthRequest);

        assertAll(
                () -> assertEquals(UNAUTHORIZED, exception.getStatusCode(), "Status code is not " + UNAUTHORIZED),
                () -> assertFalse(authResponse.getToken().isEmpty(), "Could not sign in with new password")
        );
    }

    @Test
    @DisplayName("Setup password with invalid secret")
    public void setupPasswordWithInvalidSecretTest() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest().withEmail(email);
        passwordService.resetPassword(resetPasswordRequest);

        String invalidSecret = randomUUID().toString();
        String newPassword = randomPassword();
        SetupPasswordRequest setupPasswordRequest = new SetupPasswordRequest().withPassword(newPassword).withSecret(invalidSecret);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                passwordService.setupPassword(setupPasswordRequest));

        assertEquals(NOT_FOUND, exception.getStatusCode(), "Status code is not " + NOT_FOUND);

    }

    @Test
    @DisplayName("Setup password with empty secret")
    public void setupPasswordWithEmptySecretTest() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest().withEmail(email);
        passwordService.resetPassword(resetPasswordRequest);

        String newPassword = randomPassword();
        SetupPasswordRequest setupPasswordRequest = new SetupPasswordRequest().withPassword(newPassword).withSecret("");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                passwordService.setupPassword(setupPasswordRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }

    @ParameterizedTest
    @DisplayName("Setup password with invalid format password")
    @ValueSource(strings = {"test", "test@123", "T@123", "Test!test", "Test@!$%^", ""})
    public void setupPasswordWithInvalidFormatPasswordTest(String password) {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest().withEmail(email);
        passwordService.resetPassword(resetPasswordRequest);

        String secret = mailService.getSecretFromResetPasswordConfirmationMessage();
        SetupPasswordRequest setupPasswordRequest = new SetupPasswordRequest().withPassword(password).withSecret(secret);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                passwordService.setupPassword(setupPasswordRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode(), "Status code is not " + BAD_REQUEST);
    }
}