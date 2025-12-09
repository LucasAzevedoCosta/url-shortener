package com.example.url_shortener.exception.link;

import com.example.url_shortener.exception.base.ApplicationException;

public class MaxClicksReachedException extends ApplicationException {

    public MaxClicksReachedException(long maxClicks) {
        super("O limite máximo de " + maxClicks + " cliques foi atingido.");
    }

    public String getCode() {
        return "MAX_CLICKS_REACHED";
    }
}
