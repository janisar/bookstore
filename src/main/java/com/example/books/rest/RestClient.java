package com.example.books.rest;

import static co.elastic.clients.util.ContentType.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.example.books.model.BookStoreException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class RestClient {

    private final Logger logger = Logger.getLogger(RestClient.class.getName());

    public <T> T get(String url, Class<T> responseType) {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create(url))
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        HttpResponse<String> responseString = executeRequest(client, request);
        return mapToResponse(responseType, responseString);
    }

    public <T> T post(String url, String body, Class<T> responseType) {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create(url))
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .method("POST", HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> responseString = executeRequest(client, request);
        return mapToResponse(responseType, responseString);
    }

    private HttpResponse<String> executeRequest(HttpClient client, HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.warning("Failed to execute request: " + e.getMessage());
            throw new BookStoreException("Failed to execute request");
        }
    }

    private <T> T mapToResponse(Class<T> responseType, HttpResponse<String> responseString) {
        try {
            return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                    .readValue(responseString.body(), responseType);
        } catch (JsonProcessingException e) {
            logger.warning("Failed to map response to " + responseType.getName());
        }
        return null;
    }
}
