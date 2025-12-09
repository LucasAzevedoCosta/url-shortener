package com.example.url_shortener.exception;

public class ApiKeyInactiveException extends RuntimeException {

    public ApiKeyInactiveException(String message) {
        super(message);
    }
}
