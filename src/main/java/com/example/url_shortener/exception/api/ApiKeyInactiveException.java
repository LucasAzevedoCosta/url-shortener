package com.example.url_shortener.exception.api;

import com.example.url_shortener.exception.base.ApplicationException;

public class ApiKeyInactiveException extends ApplicationException {

    public ApiKeyInactiveException() {
        super("A API Key está inativa.");
    }

    public String getCode() {
        return "API_KEY_INACTIVE";
    }
}
