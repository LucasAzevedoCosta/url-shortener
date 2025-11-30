package com.example.url_shortener.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class ApiKeyResponse {
    private UUID id;
    private String name;
    private int rateLimitPerMinute;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
