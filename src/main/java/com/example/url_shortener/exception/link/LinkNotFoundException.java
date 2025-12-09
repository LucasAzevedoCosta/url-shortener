package com.example.url_shortener.exception.link;

import com.example.url_shortener.exception.base.ApplicationException;

public class LinkNotFoundException extends ApplicationException {

    public LinkNotFoundException(String identifier) {
        super("Link não encontrado: " + identifier);
    }

    public String getCode() {
        return "LINK_NOT_FOUND";
    }
}
