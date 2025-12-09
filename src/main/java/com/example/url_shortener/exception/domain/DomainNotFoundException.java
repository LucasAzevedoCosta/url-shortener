package com.example.url_shortener.exception.domain;

import com.example.url_shortener.exception.base.ApplicationException;

public class DomainNotFoundException extends ApplicationException {

    public DomainNotFoundException(String domain) {
        super("Domínio não encontrado: " + domain);
    }

    public String getCode() {
        return "DOMAIN_NOT_FOUND";
    }
}
