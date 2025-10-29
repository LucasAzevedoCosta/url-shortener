package com.example.url_shortener.service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.url_shortener.dto.LinkCreateRequest;
import com.example.url_shortener.dto.LinkResponse;
import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.exception.DomainNotFoundException;
import com.example.url_shortener.repository.DomainRepository;
import com.example.url_shortener.repository.LinkRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final DomainRepository domainRepository;

    public LinkResponse createLink(LinkCreateRequest request) {
        // 1️⃣ Valida domínio
        Domain domain = domainRepository.findById(UUID.fromString(request.getDomainId()))
                .filter(Domain::isActive)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado ou inativo."));

        // 2️⃣ Gera shortCode único
        String shortCode = generateUniqueShortCode();

        // 3️⃣ Cria entidade Link
        Link link = new Link();
        link.setDomain(domain);
        link.setShortCode(shortCode);
        link.setOriginalUrl(request.getOriginalUrl());
        link.setExpiresAt(request.getExpiresAt());
        link.setMaxClicks(request.getMaxClicks());
        link.setIsActive(true);
        link.setMetadata("{}");
        link.setCreatedAt(Instant.now());
        link.setUpdatedAt(Instant.now());

        // Hash da senha, se fornecida
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String hash = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            link.setPasswordHash(hash);
        }

        linkRepository.save(link);

        // 4️⃣ Monta URL curta final
        String shortUrl = "https://" + domain.getHost() + "/" + shortCode;

        // 5️⃣ Retorna DTO de resposta
        return new LinkResponse(
                link.getId(),
                shortCode,
                shortUrl,
                link.getOriginalUrl(),
                link.getExpiresAt(),
                link.isActive()
        );
    }

    /** Gera um shortCode aleatório e garante unicidade no banco */
    private String generateUniqueShortCode() {
        String code;
        do {
            code = randomCode(6);
        } while (linkRepository.existsByShortCode(code));
        return code;
    }

    private String randomCode(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}
