package com.example.url_shortener.exception.link;

import com.example.url_shortener.exception.base.ApplicationException;

public class LinkExpiredException extends ApplicationException {

    public LinkExpiredException() {
        super("Este link expirou.");
    }

    public String getCode() {
        return "LINK_EXPIRED";
    }
}
