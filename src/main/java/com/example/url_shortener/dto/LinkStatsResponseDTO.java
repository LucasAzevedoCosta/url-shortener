package com.example.url_shortener.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkStatsResponseDTO {
    private long totalClicks;
    private long uniqueVisitors;
    private Instant lastClick;
    private List<TopCountry> topCountries;

    @Data
    @AllArgsConstructor
    public static class TopCountry {
        private String country;
        private long clicks;
    }
}