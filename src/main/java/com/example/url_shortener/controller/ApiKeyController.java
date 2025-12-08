package com.example.url_shortener.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.dto.apikey.ApiKeyListResponse;
import com.example.url_shortener.dto.apikey.ApiKeyResponse;
import com.example.url_shortener.dto.apikey.CreateApiKeyRequest;
import com.example.url_shortener.dto.apikey.CreateApiKeyResponse;
import com.example.url_shortener.dto.apikey.UpdateApiKeyRequest;
import com.example.url_shortener.service.ApiKeyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<CreateApiKeyResponse> create(@RequestBody CreateApiKeyRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(req.getName(), req.getRateLimitPerMinute()));
    }

    @GetMapping
    public ResponseEntity<ApiKeyListResponse> list() {
        return ResponseEntity.ok(new ApiKeyListResponse(apiKeyService.listAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(apiKeyService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateApiKeyRequest req
    ) {
        return ResponseEntity.ok(
                apiKeyService.update(id, req.getName(), req.getRateLimitPerMinute(), req.getActive())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        apiKeyService.disable(id);
        return ResponseEntity.noContent().build();
    }
}
