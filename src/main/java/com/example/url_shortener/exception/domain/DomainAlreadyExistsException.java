package com.example.url_shortener.exception.domain;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class DomainAlreadyExistsException extends ApplicationException {

    private final String domain;

    public DomainAlreadyExistsException(String domain) {
        super("Domínio já existe: " + domain, HttpStatus.BAD_REQUEST, "DOMAIN_ALREADY_EXISTS");
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
}
