package com.example.url_shortener.helper;

import com.example.url_shortener.dto.link.LinkResponse;
import com.example.url_shortener.entity.Link;

public class LinkHelpers {

    private LinkHelpers() {
    }

    public static LinkResponse toResponse(Link link) {

        String shortUrl = buildShortUrl(link);

        return new LinkResponse(
                link.getShortCode(),
                shortUrl,
                link.getOriginalUrl(),
                link.getExpiresAt(),
                link.isActive()
        );
    }

    private static String buildShortUrl(Link link) {

        String protocol = "https://";
        String host = link.getDomain().getHost();

        return protocol + host + "/" + link.getShortCode();
    }

    public static boolean isActive(Link link) {

        if (!link.isActive()) {
            return false;
        }

        if (link.getExpiresAt() == null) {
            return true;
        }

        return link.getExpiresAt().isAfter(java.time.Instant.now());
    }
}
