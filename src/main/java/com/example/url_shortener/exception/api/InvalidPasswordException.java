package com.example.url_shortener.exception.api;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class InvalidPasswordException extends ApplicationException {

    public InvalidPasswordException() {
        super("Senha inválida.", HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD");
    }
}
