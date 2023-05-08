package com.example.books.controller;

import com.example.books.model.BookStoreError;
import com.example.books.model.BookStoreException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BookStoreException.class)
    public ResponseEntity<BookStoreError> handleException(BookStoreException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BookStoreError(e.getMessage()));
    }
}
