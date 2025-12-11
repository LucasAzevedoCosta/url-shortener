package com.example.url_shortener.exception.link;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class LinkExpiredException extends ApplicationException {

    public LinkExpiredException() {
        super("Este link expirou.", HttpStatus.GONE, "LINK_EXPIRED");
    }
}
