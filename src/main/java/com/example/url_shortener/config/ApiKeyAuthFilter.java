package com.example.url_shortener.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.url_shortener.entity.ApiKey;
import com.example.url_shortener.repository.ApiKeyRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar hash SHA-256", e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.matches("^/[^/]{5,10}$")
                || path.matches("/api/links")
                || (path.equals("/api/keys") && request.getMethod().equalsIgnoreCase("POST"));

    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String apiKeyHeader = request.getHeader("X-API-KEY");

        if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
            writeUnauthorized(response, "X-API-KEY header não encontrado.");
            return;
        }

        String keyHash = sha256(apiKeyHeader);

        Optional<ApiKey> found = apiKeyRepository.findByTokenHash(keyHash);

        if (found.isEmpty()) {
            writeUnauthorized(response, "API Key inválida.");
            return;
        }

        if (!found.get().isActive()) {
            writeUnauthorized(response, "API Key desativada.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        response.getWriter().write("""
        {
            "error": "Unauthorized",
            "message": "%s"
        }
        """.formatted(message));
    }
}
