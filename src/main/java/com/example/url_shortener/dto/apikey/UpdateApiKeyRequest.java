package com.example.url_shortener.dto.apikey;

import lombok.Data;

@Data
public class UpdateApiKeyRequest {

    private String name;
    private Integer rateLimitPerMinute;
    private Boolean isActive;
}
