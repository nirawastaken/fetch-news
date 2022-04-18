package ru.nirawastaken.Application.service.utilities;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public class CustomWebClient {
    public static WebClient start (String baseUrl) throws WebClientException {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
