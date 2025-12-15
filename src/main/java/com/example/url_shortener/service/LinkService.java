package com.example.url_shortener.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.url_shortener.dto.link.LinkCreateRequest;
import com.example.url_shortener.dto.link.LinkResponse;
import com.example.url_shortener.dto.link.LinkUpdateRequest;
import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.exception.api.BadRequestException;
import com.example.url_shortener.exception.domain.DomainNotFoundException;
import com.example.url_shortener.exception.link.LinkNotFoundException;
import com.example.url_shortener.helper.LinkHelpers;
import com.example.url_shortener.helper.ShortCodeGenerator;
import com.example.url_shortener.repository.DomainRepository;
import com.example.url_shortener.repository.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final DomainRepository domainRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public LinkResponse createLink(LinkCreateRequest request) {

        Domain domain = domainRepository.findById(UUID.fromString(request.getDomainId()))
                .filter(Domain::isActive)
                .orElseThrow(() -> new DomainNotFoundException(request.getDomainId()));

        String shortCode = shortCodeGenerator.generate();

        String originalUrl = request.getOriginalUrl().trim();
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            throw new BadRequestException("URL inválida.");
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
        return LinkHelpers.toResponse(link);
    }

    public Page<LinkResponse> listLinks(Pageable pageable) {
        return linkRepository.findAll(pageable)
                .map(LinkHelpers::toResponse);
    }

    public LinkResponse getLinkByShortCode(String shortCode) {
        Link link = linkRepository.findById(shortCode)
                .orElseThrow(() -> new LinkNotFoundException(shortCode));
        return LinkHelpers.toResponse(link);
    }

    public LinkResponse updateLink(String shortCode, LinkUpdateRequest request) {
        Link link = linkRepository.findById(shortCode)
                .orElseThrow(() -> new LinkNotFoundException(shortCode));

        if (request.getOriginalUrl() != null) {
            String originalUrl = request.getOriginalUrl().trim();
            if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
                throw new BadRequestException("URL inválida.");
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

        return LinkHelpers.toResponse(link);
    }

    public void deleteLink(String shortCode) {

        Link link = linkRepository.findById(shortCode)
                .orElseThrow(() -> new LinkNotFoundException(shortCode));

        link.setIsActive(false);
        link.setDeletedAt(Instant.now());

        linkRepository.save(link);
    }
}
