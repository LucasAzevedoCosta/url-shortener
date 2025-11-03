package com.example.url_shortener.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.url_shortener.dto.LinkCreateRequest;
import com.example.url_shortener.dto.LinkResponse;
import com.example.url_shortener.dto.LinkStatsResponse;
import com.example.url_shortener.dto.LinkUpdateRequest;
import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.exception.DomainNotFoundException;
import com.example.url_shortener.exception.LinkNotFoundException;
import com.example.url_shortener.repository.DomainRepository;
import com.example.url_shortener.repository.LinkClickRepository;
import com.example.url_shortener.repository.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final DomainRepository domainRepository;
    private final LinkClickRepository linkClickRepository;

    public LinkResponse createLink(LinkCreateRequest request) {
        Domain domain = domainRepository.findById(UUID.fromString(request.getDomainId()))
                .filter(Domain::isActive)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado ou inativo."));

        String shortCode = generateUniqueShortCode();

        String originalUrl = request.getOriginalUrl().trim();
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        Link link = new Link();
        link.setDomain(domain);
        link.setShortCode(shortCode);
        link.setOriginalUrl(originalUrl);
        link.setExpiresAt(request.getExpiresAt());
        link.setMaxClicks(request.getMaxClicks());
        link.setIsActive(true);
        link.setMetadata("{}");
        link.setCreatedAt(Instant.now());
        link.setUpdatedAt(Instant.now());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            link.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        linkRepository.save(link);

        return toResponse(link);
    }

    public Page<LinkResponse> listLinks(Pageable pageable) {
        return linkRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public LinkResponse getLinkById(UUID id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotFoundException("Link não encontrado."));
        return toResponse(link);
    }

    public LinkResponse updateLink(UUID id, LinkUpdateRequest request) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotFoundException("Link não encontrado."));

        if (request.getOriginalUrl() != null) {
            String originalUrl = request.getOriginalUrl().trim();
            if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
                originalUrl = "https://" + originalUrl;
            }
            link.setOriginalUrl(originalUrl);
        }

        if (request.getExpiresAt() != null) {
            link.setExpiresAt(request.getExpiresAt());
        }
        if (request.getMaxClicks() != null) {
            link.setMaxClicks(request.getMaxClicks());
        }
        if (request.getIsActive() != null) {
            link.setIsActive(request.getIsActive());
        }
        if (request.getPassword() != null) {
            link.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        link.setUpdatedAt(Instant.now());
        linkRepository.save(link);

        return toResponse(link);
    }

    public void deleteLink(UUID id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotFoundException("Link não encontrado."));
        link.setIsActive(false);
        link.setDeletedAt(Instant.now());
        linkRepository.save(link);
    }

    public LinkStatsResponse getLinkStats(UUID id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotFoundException("Link não encontrado."));

        long totalClicks = linkClickRepository.countByLink_Id(id);

        Optional<Instant> lastClick = linkClickRepository.findByLink_Id(id)
                .stream()
                .map(c -> c.getClickedAt())
                .max(Instant::compareTo);

        return new LinkStatsResponse(
                link.getId(),
                totalClicks,
                lastClick.orElse(null),
                link.getCreatedAt(),
                link.getOriginalUrl()
        );
    }

    // 🔧 Helpers
    private String generateUniqueShortCode() {
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

    private LinkResponse toResponse(Link link) {
        String shortUrl = "https://" + link.getDomain().getHost() + "/" + link.getShortCode();
        return new LinkResponse(
                link.getId(),
                link.getShortCode(),
                shortUrl,
                link.getOriginalUrl(),
                link.getExpiresAt(),
                link.isActive()
        );
    }
}
