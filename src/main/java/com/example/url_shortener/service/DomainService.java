package com.example.url_shortener.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.url_shortener.dto.DomainCreateRequest;
import com.example.url_shortener.dto.DomainResponse;
import com.example.url_shortener.dto.DomainUpdateRequest;
import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.exception.DomainAlreadyExistsException;
import com.example.url_shortener.exception.DomainNotFoundException;
import com.example.url_shortener.repository.DomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DomainService {

    private final DomainRepository domainRepository;

    public DomainResponse createDomain(DomainCreateRequest request) {
        if (domainRepository.existsByHost(request.getHost())) {
            throw new DomainAlreadyExistsException("O domínio já está cadastrado: " + request.getHost());
        }

        Domain domain = new Domain();
        domain.setHost(request.getHost());
        domain.setActive(true);
        domain.setCreatedAt(Instant.now());
        domain.setUpdatedAt(Instant.now());

        domainRepository.save(domain);
        return toResponse(domain);
    }

    public List<DomainResponse> listDomains() {
        return domainRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DomainResponse getDomainById(UUID id) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado."));
        return toResponse(domain);
    }

    public DomainResponse updateDomain(UUID id, DomainUpdateRequest request) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado."));

        if (request.getIsActive() != null) {
            domain.setActive(request.getIsActive());
        }

        domain.setUpdatedAt(Instant.now());
        domainRepository.save(domain);

        return toResponse(domain);
    }

    public void deleteDomain(UUID id) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado."));

        domain.setActive(false);
        domain.setUpdatedAt(Instant.now());
        domainRepository.save(domain);
    }

    // =========================================================
    // 🔧 Helper

    private DomainResponse toResponse(Domain domain) {
        return new DomainResponse(
                domain.getId(),
                domain.getHost(),
                domain.isActive(),
                domain.getCreatedAt()
        );
    }
}
