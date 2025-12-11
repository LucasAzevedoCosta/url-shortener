package com.example.url_shortener.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyGenerator {

    private final CodeGenerator generator;

    public ApiKeyGenerator(
            @Value("${app.apikey.secret}") String secret,
            @Value("${app.apikey.start-seq:100000}") long startValue,
            @Value("${app.apikey.min-length:32}") int minLength,
            @Value("${app.apikey.max-length:64}") int maxLength
    ) {
        this.generator = new CodeGenerator(secret, startValue, minLength, maxLength);
    }

    public String generateApiKey() {
        return generator.generate();
    }
}
