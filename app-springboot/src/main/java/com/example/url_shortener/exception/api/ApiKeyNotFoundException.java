package com.example.url_shortener.exception.api;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class ApiKeyNotFoundException extends ApplicationException {

    public ApiKeyNotFoundException() {
        super("A API Key não foi encontrada.", HttpStatus.NOT_FOUND, "API_KEY_NOT_FOUND");
    }
}
