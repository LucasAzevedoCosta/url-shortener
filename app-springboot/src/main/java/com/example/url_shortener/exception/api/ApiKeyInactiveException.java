package com.example.url_shortener.exception.api;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class ApiKeyInactiveException extends ApplicationException {

    public ApiKeyInactiveException() {
        super("A API Key está inativa.", HttpStatus.FORBIDDEN, "API_KEY_INACTIVE");
    }
}
