package com.example.url_shortener.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.url_shortener.entity.Domain;

@Repository
public interface DomainRepository extends JpaRepository<Domain, UUID> {

    Optional<Domain> findByHost(String host);

    boolean existsByHost(String host);
}
