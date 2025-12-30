package com.example.url_shortener.dto.apikey;

import lombok.Data;

@Data
public class CreateApiKeyRequest {

    private String name;
    private int rateLimitPerMinute;
}
