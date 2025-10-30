package com.example.url_shortener.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.entity.LinkClick;
import com.example.url_shortener.exception.DomainNotFoundException;
import com.example.url_shortener.exception.LinkNotFoundException;
import com.example.url_shortener.repository.DomainRepository;
import com.example.url_shortener.repository.LinkClickRepository;
import com.example.url_shortener.repository.LinkRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RedirectService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkClickRepository linkClickRepository;

    public ResponseEntity<?> handleRedirect(String host, String shortCode, HttpServletRequest request) {
        Domain domain = domainRepository.findByHost(host)
                .filter(Domain::isActive)
                .orElseThrow(() -> new DomainNotFoundException("Domínio inválido ou inativo: " + host));

        Link link = linkRepository.findByShortCodeAndDomain_Id(shortCode, domain.getId())
                .orElseThrow(() -> new LinkNotFoundException("Link não encontrado ou não pertence ao domínio."));

        if (!link.isActive()) {
            return ResponseEntity.status(HttpStatus.GONE).body("Este link foi desativado.");
        }

        if (link.getExpiresAt() != null && Instant.now().isAfter(link.getExpiresAt())) {
            return ResponseEntity.status(HttpStatus.GONE).body("Este link expirou.");
        }

        if (link.getMaxClicks() != null && link.getClickCount() >= link.getMaxClicks()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Limite de cliques atingido.");
        }

        LinkClick click = new LinkClick();
        click.setLink(link);
        click.setClickedAt(Instant.now());
        click.setIp(request.getRemoteAddr());
        click.setUserAgent(request.getHeader("User-Agent"));
        click.setReferrer(request.getHeader("Referer"));

        linkClickRepository.save(click);

        link.setClickCount(link.getClickCount() + 1);
        linkRepository.save(link);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, link.getOriginalUrl())
                .build();
    }
}
