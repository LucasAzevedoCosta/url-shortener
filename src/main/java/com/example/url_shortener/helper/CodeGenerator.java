package com.example.url_shortener.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CodeGenerator {

    private static final String BASE_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = 62;

    private final char[] shuffledAlphabet;
    private final AtomicLong sequence;

    private final int minLength;
    private final int maxLength;

    private final long minValue;
    private final long maxValue;

    public CodeGenerator(String secret, long startValue, int minLength, int maxLength) {

        if (minLength < 1 || minLength > maxLength) {
            throw new IllegalArgumentException("minLength deve ser >= 1 e <= maxLength");
        }

        this.minLength = minLength;
        this.maxLength = maxLength;

        this.minValue = getMinValueForLength(minLength);
        this.maxValue = getMaxValueForLength(maxLength);

        this.shuffledAlphabet = shuffleAlphabetDeterministic(BASE_ALPHABET, secret);

        long initial = Math.max(startValue, minValue);
        this.sequence = new AtomicLong(initial);
    }

    public String generate() {
        long current = sequence.getAndIncrement();

        if (current > maxValue) {
            throw new IllegalStateException("Limite máximo de códigos atingido.");
        }

        return encodeBase62WithMinLength(current);
    }

    private String encodeBase62WithMinLength(long value) {
        String encoded = encodeBase62(value);

        if (encoded.length() < minLength) {
            int diff = minLength - encoded.length();
            return String.valueOf(shuffledAlphabet[0]).repeat(diff) + encoded;
        }
        return encoded;
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
                seed = (seed << 8) | (digest[i] & 255L);
            }
            return seed;

        } catch (NoSuchAlgorithmException e) {
            return secret.hashCode();
        }
    }

    private long getMinValueForLength(int length) {
        return (long) Math.pow(BASE, length - 1);
    }

    private long getMaxValueForLength(int length) {
        return (long) Math.pow(BASE, length) - 1;
    }
}
