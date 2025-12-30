package com.example.url_shortener.dto.link;

import java.time.Instant;

import lombok.Data;

@Data
public class LinkUpdateRequest {
    private String originalUrl;
    private Instant expiresAt;
    private Long maxClicks;
    private Boolean isActive;
    private String password;
}
