package com.example.url_shortener.exception.api;

import com.example.url_shortener.exception.base.ApplicationException;

public class InvalidPasswordException extends ApplicationException {

    public InvalidPasswordException() {
        super("Senha inválida.");
    }

    public String getCode() {
        return "INVALID_PASSWORD";
    }
}
