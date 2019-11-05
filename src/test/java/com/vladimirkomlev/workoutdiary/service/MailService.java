package com.vladimirkomlev.workoutdiary.service;

import com.vladimirkomlev.workoutdiary.model.AllMessagesResponse;
import com.vladimirkomlev.workoutdiary.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static java.lang.Thread.sleep;

@Service
public class MailService {
    private final RestTemplate restTemplate;
    private final EnvironmentService environmentService;
    private static int readMessagesCount = 0;

    private final static int RECEIVE_MESSAGE_MAX_ATTEMPTS = 10;
    private final static long DELAY_BETWEEN_EMAIL_ATTEMPTS_MILLIS = 500L;
    private final static int ALL_MESSAGES_LIMIT = 1000;
    private final static int LAST_MESSAGE_LIMIT = 1;

    @Autowired
    public MailService(RestTemplate restTemplate, EnvironmentService environmentService) {
        this.restTemplate = restTemplate;
        this.environmentService = environmentService;
    }

    public AllMessagesResponse getMessages(int limit) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(environmentService.mailhogUrl() + "/api/v2/messages")
                .queryParam("limit", limit);
        UriComponents uriComponents = builder.build().encode();
        return restTemplate.getForObject(uriComponents.toUri(), AllMessagesResponse.class);
    }

    public Item waitAndReceiveMessage() {
        int messageCount;
        for (int i = 0; i <= RECEIVE_MESSAGE_MAX_ATTEMPTS; i++) {
            messageCount = getMessages(ALL_MESSAGES_LIMIT).getCount();
            if (messageCount > readMessagesCount) {
                readMessagesCount = messageCount;
                return getMessages(LAST_MESSAGE_LIMIT).getItems().get(0);
            }
            System.out.println("Wait until message is received " + i);
            try {
                sleep(DELAY_BETWEEN_EMAIL_ATTEMPTS_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("Item is not received");
    }

    public String getConfirmationSecretFromMessage() {
        Item message = waitAndReceiveMessage();
        return message.getBody().replace("Your confirmation secret is: ", "");
    }
}
