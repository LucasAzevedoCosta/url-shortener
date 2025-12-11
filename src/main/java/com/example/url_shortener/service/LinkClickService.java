package com.example.url_shortener.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.url_shortener.dto.link.LinkClickResponse;
import com.example.url_shortener.dto.link.LinkStatsResponse;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.entity.LinkClick;
import com.example.url_shortener.exception.link.LinkNotFoundException;
import com.example.url_shortener.repository.LinkClickRepository;
import com.example.url_shortener.repository.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkClickService {

    private final LinkClickRepository linkClickRepository;
    private final LinkRepository linkRepository;

    @Transactional(readOnly = true)
    public List<LinkClickResponse> listClicks(String linkId, Instant start, Instant end) {

        Link link = linkRepository.findById(linkId)
                .orElseThrow(() -> new LinkNotFoundException(linkId));

        List<LinkClick> clicks
                = (start != null && end != null)
                        ? linkClickRepository.findByLinkAndClickedAtBetween(link, start, end)
                        : linkClickRepository.findByLink(link);

        return clicks.stream()
                .map(LinkClickResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LinkStatsResponse getLinkStats(String linkId) {

        Link link = linkRepository.findById(linkId)
                .orElseThrow(() -> new LinkNotFoundException(linkId));

        long totalClicks = linkClickRepository.countByLink(link);
        long uniqueVisitors = linkClickRepository.countUniqueVisitorsByLink(link);

        var countryData = linkClickRepository.countClicksByCountry(link);
        var topCountries = countryData.stream()
                .map(row -> new LinkStatsResponse.TopCountry((String) row[0], (Long) row[1]))
                .toList();

        Instant lastClick = linkClickRepository.findByLink(link).stream()
                .map(LinkClick::getClickedAt)
                .max(Instant::compareTo)
                .orElse(null);

        return new LinkStatsResponse(totalClicks, uniqueVisitors, lastClick, topCountries);
    }
}
