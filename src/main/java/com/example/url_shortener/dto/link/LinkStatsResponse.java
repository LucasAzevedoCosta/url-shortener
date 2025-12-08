package com.example.url_shortener.dto.link;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkStatsResponse {
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