package com.example.url_shortener.dto.link;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkResponse {
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Instant expiresAt;
    private boolean isActive;
}
