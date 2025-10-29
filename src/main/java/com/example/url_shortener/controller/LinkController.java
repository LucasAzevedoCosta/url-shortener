package com.example.url_shortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.dto.LinkCreateRequest;
import com.example.url_shortener.dto.LinkResponse;
import com.example.url_shortener.service.LinkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<LinkResponse> createLink(@RequestBody LinkCreateRequest request) {
        LinkResponse response = linkService.createLink(request);
        return ResponseEntity.ok(response);
    }
}
