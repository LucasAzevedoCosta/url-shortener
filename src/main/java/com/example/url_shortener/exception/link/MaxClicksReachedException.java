package com.example.url_shortener.exception.link;

import org.springframework.http.HttpStatus;

import com.example.url_shortener.exception.base.ApplicationException;

public class MaxClicksReachedException extends ApplicationException {

    public MaxClicksReachedException(long maxClicks) {
        super("O limite máximo de " + maxClicks + " cliques foi atingido.", HttpStatus.TOO_MANY_REQUESTS, "MAX_CLICKS_REACHED");
    }
}
