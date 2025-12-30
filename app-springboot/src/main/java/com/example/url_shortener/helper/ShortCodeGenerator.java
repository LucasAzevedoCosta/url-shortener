package com.example.url_shortener.helper;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeGenerator {

    private static final int BASE = 62;

    private final char[] alphabet;
    private final AtomicLong sequence;

    private final int minLength;
    private final int maxLength;

    private final long minValue;
    private final long maxValue;

    public ShortCodeGenerator(
            @Value("${app.shortener.secret}") String secret,
            @Value("${app.shortener.start-seq:1}") long startValue,
            @Value("${app.shortener.min-length:3}") int minLength,
            @Value("${app.shortener.max-length:8}") int maxLength
    ) {

        if (minLength < 1 || minLength > maxLength) {
            throw new IllegalArgumentException("min-length inválido");
        }

        this.minLength = minLength;
        this.maxLength = maxLength;

        this.minValue = (long) Math.pow(BASE, minLength - 1);
        this.maxValue = (long) Math.pow(BASE, maxLength) - 1;

        this.alphabet = shuffleAlphabetDeterministic(
                Alphabets.BASE62,
                secret
        );

        long initial = Math.max(startValue, minValue);
        this.sequence = new AtomicLong(initial);
    }

    public String generate() {
        long value = sequence.getAndIncrement();

        if (value > maxValue) {
            throw new IllegalStateException("Limite máximo de short codes atingido");
        }

        return encodeBase62(value);
    }

    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(alphabet[(int) (value % BASE)]);
            value /= BASE;
        }

        while (sb.length() < minLength) {
            sb.append(alphabet[0]);
        }

        return sb.reverse().toString();
    }

    private char[] shuffleAlphabetDeterministic(String alphabet, String secret) {
        char[] arr = alphabet.toCharArray();
        Random rnd = new Random(secret.hashCode());

        for (int i = arr.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            char tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }
}
