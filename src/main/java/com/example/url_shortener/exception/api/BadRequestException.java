package com.example.url_shortener.exception.api;

import com.example.url_shortener.exception.base.ApplicationException;

public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(message);
    }

    public String getCode() {
        return "BAD_REQUEST";
    }
}
