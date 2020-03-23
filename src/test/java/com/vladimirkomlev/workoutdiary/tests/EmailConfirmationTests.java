package com.vladimirkomlev.workoutdiary.tests;

import com.vladimirkomlev.workoutdiary.model.ConfirmationRequest;
import com.vladimirkomlev.workoutdiary.model.UserCreateRequest;
import com.vladimirkomlev.workoutdiary.model.UserResponse;
import com.vladimirkomlev.workoutdiary.service.EmailConfirmationService;
import com.vladimirkomlev.workoutdiary.service.MailService;
import com.vladimirkomlev.workoutdiary.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static com.vladimirkomlev.workoutdiary.generator.DataGenerator.randomUserCreateRequest;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
public class EmailConfirmationTests {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MailService mailService;

    @Autowired
    private EmailConfirmationService emailConfirmationService;

    @Test
    @DisplayName("Confirm email with valid code")
    public void confirmEmailWithValidCodeTest() {
        UserCreateRequest userCreateRequest = randomUserCreateRequest();
        UserResponse registrationResponse = registrationService.signUp(userCreateRequest);
        String code = mailService.getConfirmationCodeFromEmailConfirmationMessage();
        ConfirmationRequest confirmationRequest = new ConfirmationRequest().withCode(code);
        UserResponse confirmationResponse = emailConfirmationService.confirm(confirmationRequest);

        assertEquals(registrationResponse, confirmationResponse, "Email is not confirmed");
    }

    @Test
    @DisplayName("Confirm email with invalid code")
    public void confirmEmailWithInvalidCodeTest() {
        UserCreateRequest userCreateRequest = randomUserCreateRequest();
        UserResponse registrationResponse = registrationService.signUp(userCreateRequest);
        String code = randomUUID().toString();
        ConfirmationRequest confirmationRequest = new ConfirmationRequest().withCode(code);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                emailConfirmationService.confirm(confirmationRequest));

        assertEquals(NOT_FOUND, exception.getStatusCode(), "Status code is not " + NOT_FOUND);
    }
}