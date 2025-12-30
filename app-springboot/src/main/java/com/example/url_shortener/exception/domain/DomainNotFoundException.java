package com.example.url_shortener.exception.domain;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class DomainNotFoundException extends ApplicationException {

    public DomainNotFoundException(String domain) {
        super("Domínio não encontrado: " + domain, HttpStatus.NOT_FOUND, "DOMAIN_NOT_FOUND");
    }
}
