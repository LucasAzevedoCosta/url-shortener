package com.example.url_shortener.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.url_shortener.entity.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {

    Optional<Link> findByShortCode(String shortCode);

    Optional<Link> findByOriginalUrl(String originalUrl);

    long countByDomain_Id(UUID domainId);

    long countByIsActiveTrue();
}
