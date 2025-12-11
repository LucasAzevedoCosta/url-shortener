package com.example.url_shortener.exception.link;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class LinkInactiveException extends ApplicationException {

    public LinkInactiveException() {
        super("Este link está desativado.", HttpStatus.FORBIDDEN, "LINK_INACTIVE");
    }
}
