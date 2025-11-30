package com.example.url_shortener.dto;

import lombok.Data;

@Data
public class UpdateApiKeyRequest {

    private String name;
    private Integer rateLimitPerMinute;
    private Boolean active;
}
