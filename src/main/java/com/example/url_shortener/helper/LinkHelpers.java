package com.example.url_shortener.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.url_shortener.dto.link.LinkResponse;
import com.example.url_shortener.entity.Link;

@Component
public class LinkHelpers {

    private static final String BASE_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = 62;
    private static final int MAX_LENGTH = 7;

    private final char[] shuffledAlphabet;
    private final AtomicLong sequence;

    public LinkHelpers(@Value("${app.shortener.secret}") String secret,
            @Value("${app.shortener.start-seq:1}") long startValue) {
        this.shuffledAlphabet = shuffleAlphabetDeterministic(BASE_ALPHABET, secret);
        this.sequence = new AtomicLong(startValue);
    }

    public String generateNextShortCode() {
        long currentValue = sequence.getAndIncrement();
        if (currentValue > getMaxValueForLength(MAX_LENGTH)) {
            throw new IllegalStateException("Limite máximo de códigos atingido (62^7 - 1).");
        }
        return encodeBase62(currentValue);
    }

    private String encodeBase62(long value) {
        if (value == 0) {
            return String.valueOf(shuffledAlphabet[0]);
        }
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            int idx = (int) (value % BASE);
            sb.append(shuffledAlphabet[idx]);
            value /= BASE;
        }
        return sb.reverse().toString();
    }

    private char[] shuffleAlphabetDeterministic(String alphabet, String secret) {
        char[] arr = alphabet.toCharArray();
        long seed = deriveSeedFromSecret(secret);
        Random rnd = new Random(seed);
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            char tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }

    private long deriveSeedFromSecret(String secret) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(secret.getBytes(StandardCharsets.UTF_8));
            long seed = 0L;
            for (int i = 0; i < Math.min(8, digest.length); i++) {
                seed = (seed << 8) | (digest[i] & 0xffL);
            }
            return seed;
        } catch (NoSuchAlgorithmException e) {
            return secret.hashCode();
        }
    }

    private long getMaxValueForLength(int length) {
        return (long) Math.pow(BASE, length) - 1;
    }

    public static LinkResponse toResponse(Link link) {
        String shortUrl = "https://" + link.getDomain().getHost() + "/" + link.getShortCode();
        return new LinkResponse(
                link.getShortCode(),
                shortUrl,
                link.getOriginalUrl(),
                link.getExpiresAt(),
                link.isActive()
        );
    }
}
