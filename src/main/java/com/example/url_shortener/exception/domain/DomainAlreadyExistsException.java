package com.example.url_shortener.exception.domain;

import com.example.url_shortener.exception.base.ApplicationException;

public class DomainAlreadyExistsException extends ApplicationException {

    private final String domain;

    public DomainAlreadyExistsException(String domain) {
        super("Domínio já existe: " + domain);
        this.domain = domain;
    }

    public String getCode() {
        return "DOMAIN_ALREADY_EXISTS";
    }

    public String getDomain() {
        return domain;
    }
}
