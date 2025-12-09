package com.example.url_shortener.exception.api;

import com.example.url_shortener.exception.base.ApplicationException;

public class ApiKeyNotFoundException extends ApplicationException {

    public ApiKeyNotFoundException() {
        super("A API Key não foi encontrada.");
    }

    public String getCode() {
        return "API_KEY_NOT_FOUND";
    }
}
