package com.example.url_shortener.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkStatsResponse {
    private UUID id;
    private long totalClicks;
    private Instant lastClick;
    private Instant createdAt;
    private String originalUrl;
}

