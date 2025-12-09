package com.example.url_shortener.exception;

public class ApiKeyNotFoundException extends RuntimeException {

    public ApiKeyNotFoundException(String message) {
        super(message);
    }
}
