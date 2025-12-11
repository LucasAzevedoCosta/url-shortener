package com.example.url_shortener.exception.link;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class LinkNotFoundException extends ApplicationException {

    public LinkNotFoundException(String identifier) {
        super("Link não encontrado: " + identifier, HttpStatus.NOT_FOUND, "LINK_NOT_FOUND");
    }
}
