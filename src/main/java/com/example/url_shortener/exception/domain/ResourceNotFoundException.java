package com.example.url_shortener.exception.domain;

import com.example.url_shortener.exception.base.ApplicationException;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String resource) {
        super("Recurso não encontrado: " + resource);
    }

    public String getCode() {
        return "RESOURCE_NOT_FOUND";
    }
}
