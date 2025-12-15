package com.example.url_shortener.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.url_shortener.dto.apikey.ApiKeyResponse;
import com.example.url_shortener.dto.apikey.CreateApiKeyResponse;
import com.example.url_shortener.entity.ApiKey;
import com.example.url_shortener.exception.api.ApiKeyInactiveException;
import com.example.url_shortener.exception.api.ApiKeyNotFoundException;
import com.example.url_shortener.exception.api.BadRequestException;
import com.example.url_shortener.exception.common.HashGenerationException;
import com.example.url_shortener.helper.ApiKeyGenerator;
import com.example.url_shortener.repository.ApiKeyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGenerator apiKeyGenerator;

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new HashGenerationException("Erro ao gerar hash da API Key");
        }
    }

    public CreateApiKeyResponse create(String name, int rateLimit) {

        if (name == null || name.isBlank()) {
            throw new BadRequestException("Nome inválido");
        }

        String rawKey = apiKeyGenerator.generateApiKey();
        String hash = sha256(rawKey);

        ApiKey apiKey = new ApiKey();
        apiKey.setName(name);
        apiKey.setTokenHash(hash);
        apiKey.setRateLimitPerMinute(rateLimit);
        apiKey.setActive(true);
        apiKey.setCreatedAt(Instant.now());
        apiKey.setUpdatedAt(Instant.now());

        apiKeyRepository.save(apiKey);

        CreateApiKeyResponse response = new CreateApiKeyResponse();
        response.setId(apiKey.getId());
        response.setName(apiKey.getName());
        response.setApiKey(rawKey);
        response.setRateLimitPerMinute(apiKey.getRateLimitPerMinute());
        response.setCreatedAt(apiKey.getCreatedAt());

        return response;
    }

    public List<ApiKeyResponse> listAll() {
        return apiKeyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ApiKeyResponse findById(UUID id) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(ApiKeyNotFoundException::new);
        return toResponse(key);
    }

    public ApiKeyResponse update(UUID id, String name, Integer rateLimit, Boolean active) {

        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(ApiKeyNotFoundException::new);

        if (name != null && name.isBlank()) {
            throw new BadRequestException("Nome inválido");
        }

        if (name != null) {
            key.setName(name);
        }
        if (rateLimit != null) {
            key.setRateLimitPerMinute(rateLimit);
        }
        if (active != null) {
            key.setActive(active);
        }

        key.setUpdatedAt(Instant.now());
        apiKeyRepository.save(key);

        return toResponse(key);
    }

    public void disable(UUID id) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(ApiKeyNotFoundException::new);

        if (!key.isActive()) {
            throw new ApiKeyInactiveException();
        }

        key.setActive(false);
        key.setUpdatedAt(Instant.now());
        apiKeyRepository.save(key);
    }

    private ApiKeyResponse toResponse(ApiKey key) {
        ApiKeyResponse dto = new ApiKeyResponse();
        dto.setId(key.getId());
        dto.setName(key.getName());
        dto.setRateLimitPerMinute(key.getRateLimitPerMinute());
        dto.setActive(key.isActive());
        dto.setCreatedAt(key.getCreatedAt());
        dto.setUpdatedAt(key.getUpdatedAt());
        return dto;
    }
}
