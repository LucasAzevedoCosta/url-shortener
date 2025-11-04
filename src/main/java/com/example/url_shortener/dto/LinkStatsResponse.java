package com.example.url_shortener.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkStatsResponse {
    private String shortCode;
    private long totalClicks;
    private Instant lastClick;
    private Instant createdAt;
    private String originalUrl;
}

