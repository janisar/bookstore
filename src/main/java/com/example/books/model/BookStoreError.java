package com.example.books.model;

import org.joda.time.DateTime;

public class BookStoreError {

    private final String message;
    private final String timestamp;

    public BookStoreError(String message) {
        this.message = message;
        this.timestamp = DateTime.now().toString();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
