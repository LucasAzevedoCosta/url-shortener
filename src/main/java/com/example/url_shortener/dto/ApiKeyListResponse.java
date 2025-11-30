package com.example.url_shortener.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiKeyListResponse {

    private List<ApiKeyResponse> keys;
}
