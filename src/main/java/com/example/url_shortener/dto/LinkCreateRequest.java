package com.example.url_shortener.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class LinkCreateRequest {
    private String originalUrl;
    private String domainId;
    private Instant expiresAt;
    private Long maxClicks;
    private String password;
}
