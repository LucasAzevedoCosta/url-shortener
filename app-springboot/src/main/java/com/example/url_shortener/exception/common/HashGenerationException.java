package com.example.url_shortener.exception.common;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class HashGenerationException extends ApplicationException {

    private static final String CODE = "HASH_GENERATION_ERROR";

    public HashGenerationException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, CODE);
    }
}
