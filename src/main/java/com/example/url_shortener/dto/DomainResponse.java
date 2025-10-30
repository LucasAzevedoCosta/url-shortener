package com.example.url_shortener.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainResponse {
    private UUID id;
    private String host;
    private Boolean isActive;
    private Instant createdAt;
}
