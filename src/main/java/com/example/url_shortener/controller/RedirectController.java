package com.example.url_shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.service.RedirectService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RedirectController {

    @Autowired
    private RedirectService redirectService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(
            @PathVariable String shortCode,
            @RequestHeader("Host") String host,
            HttpServletRequest request
    ) {
        return redirectService.handleRedirect(host, shortCode, request);
    }
}
