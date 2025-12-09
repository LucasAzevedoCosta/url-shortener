package com.example.url_shortener.exception.link;

import com.example.url_shortener.exception.base.ApplicationException;

public class LinkInactiveException extends ApplicationException {

    public LinkInactiveException() {
        super("Este link está desativado.");
    }

    public String getCode() {
        return "LINK_INACTIVE";
    }
}
