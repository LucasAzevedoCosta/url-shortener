package com.example.url_shortener.exception.domain;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String resource) {
        super("Recurso não encontrado: " + resource, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
}
