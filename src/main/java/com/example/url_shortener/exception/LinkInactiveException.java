package com.example.url_shortener.exception;

public class LinkInactiveException extends RuntimeException {

    public LinkInactiveException(String message) {
        super(message);
    }
}
