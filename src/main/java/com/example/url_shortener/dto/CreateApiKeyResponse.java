package com.example.url_shortener.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateApiKeyResponse {

    private UUID id;
    private String name;
    private String apiKey; // token real
    private int rateLimitPerMinute;
    private Instant createdAt;
}
