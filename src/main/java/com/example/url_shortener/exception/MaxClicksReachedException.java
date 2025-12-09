package com.example.url_shortener.exception;

public class MaxClicksReachedException extends RuntimeException {

    public MaxClicksReachedException(String message) {
        super(message);
    }
}
