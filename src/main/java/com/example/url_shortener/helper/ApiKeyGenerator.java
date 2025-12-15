package com.example.url_shortener.helper;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyGenerator {

    private static final int BASE = 64;

    private final char[] alphabet;
    private final AtomicLong sequence;
    private final int length;
    private final String secret;

    public ApiKeyGenerator(
            @Value("${app.apikey.secret}") String secret,
            @Value("${app.apikey.start-seq:1}") long startValue,
            @Value("${app.apikey.length:40}") int length
    ) {
        this.secret = secret;
        this.length = length;
        this.sequence = new AtomicLong(startValue);
        this.alphabet = shuffleAlphabetDeterministic(
                Alphabets.BASE64_URL_SAFE,
                secret
        );
    }

    public String generateApiKey() {
        long seq = sequence.getAndIncrement();
        byte[] hash = sha256(seq + ":" + secret);
        return encode(hash);
    }

    private byte[] sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash da API Key");
        }
    }

    private String encode(byte[] data) {
        BigInteger num = new BigInteger(1, data);
        StringBuilder sb = new StringBuilder();

        while (num.compareTo(BigInteger.ZERO) > 0 && sb.length() < length) {
            BigInteger[] divRem
                    = num.divideAndRemainder(BigInteger.valueOf(BASE));

            sb.append(alphabet[divRem[1].intValue()]);
            num = divRem[0];
        }

        while (sb.length() < length) {
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
