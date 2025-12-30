package com.example.url_shortener.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.url_shortener.entity.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    Optional<ApiKey> findByTokenHash(String tokenHash);

    Optional<ApiKey> findByName(String name);

    boolean existsByTokenHash(String tokenHash);

    boolean existsByName(String name);
}
