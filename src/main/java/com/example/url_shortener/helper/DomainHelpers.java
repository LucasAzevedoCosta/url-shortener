package com.example.url_shortener.helper;

import com.example.url_shortener.dto.domain.DomainResponse;
import com.example.url_shortener.entity.Domain;

public class DomainHelpers {

    public static DomainResponse toResponse(Domain domain) {
        return new DomainResponse(
                domain.getId(),
                domain.getHost(),
                domain.isActive(),
                domain.getCreatedAt()
        );
    }
}

