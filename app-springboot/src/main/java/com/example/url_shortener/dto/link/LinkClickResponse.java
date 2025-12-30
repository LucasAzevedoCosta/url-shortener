package com.example.url_shortener.dto.link;

import java.time.Instant;

import com.example.url_shortener.entity.LinkClick;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkClickResponse {

    private Long id;
    private Instant clickedAt;
    private String ip;
    private String userAgent;
    private String referrer;
    private String country;
    private String city;

    public static LinkClickResponse fromEntity(LinkClick entity) {
        return new LinkClickResponse(
                entity.getId(),
                entity.getClickedAt(),
                entity.getIp(),
                entity.getUserAgent(),
                entity.getReferrer(),
                entity.getCountry(),
                entity.getCity()
        );
    }
}

