package com.example.url_shortener.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkResponse {
    private UUID id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Instant expiresAt;
    private boolean isActive;
}
