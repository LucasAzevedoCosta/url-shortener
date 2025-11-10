package com.example.url_shortener.dto;

import java.time.Instant;

import com.example.url_shortener.entity.LinkClick;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkClickResponseDTO {

    private Long id;
    private Instant clickedAt;
    private String ip;
    private String userAgent;
    private String referrer;
    private String country;
    private String city;

    public static LinkClickResponseDTO fromEntity(LinkClick entity) {
        return new LinkClickResponseDTO(
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

