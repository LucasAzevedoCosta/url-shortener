package com.example.url_shortener.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.url_shortener.dto.domain.DomainCreateRequest;
import com.example.url_shortener.dto.domain.DomainResponse;
import com.example.url_shortener.dto.domain.DomainUpdateRequest;
import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.exception.domain.DomainAlreadyExistsException;
import com.example.url_shortener.exception.domain.DomainNotFoundException;
import com.example.url_shortener.helper.DomainHelpers;
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
        return DomainHelpers.toResponse(domain);
    }

    public List<DomainResponse> listDomains() {
        return domainRepository.findAll()
                .stream()
                .map(DomainHelpers::toResponse)
                .collect(Collectors.toList());
    }

    public DomainResponse getDomainById(UUID id) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado."));
        return DomainHelpers.toResponse(domain);
    }

    public DomainResponse updateDomain(UUID id, DomainUpdateRequest request) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado."));

        if (request.getIsActive() != null) {
            domain.setActive(request.getIsActive());
        }

        domain.setUpdatedAt(Instant.now());
        domainRepository.save(domain);

        return DomainHelpers.toResponse(domain);
    }

    public void deleteDomain(UUID id) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Domínio não encontrado."));

        domain.setActive(false);
        domain.setUpdatedAt(Instant.now());
        domainRepository.save(domain);
    }
}
