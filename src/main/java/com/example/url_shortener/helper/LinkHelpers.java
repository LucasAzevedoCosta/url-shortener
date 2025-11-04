package com.example.url_shortener.helper;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.example.url_shortener.dto.LinkResponse;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.repository.LinkRepository;

@Component
public class LinkHelpers {

    public String generateUniqueShortCode(LinkRepository linkRepository) {
        String code;
        do {
            code = randomCode(6);
        } while (linkRepository.existsByShortCode(code));
        return code;
    }

    private String randomCode(int len) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int idx = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
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
