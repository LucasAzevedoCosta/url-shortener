package com.example.url_shortener.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.url_shortener.entity.LinkClick;

@Repository
public interface LinkClickRepository extends JpaRepository<LinkClick, Long> {

    List<LinkClick> findByLink_IdAndClickedAtBetween(UUID linkId, Instant start, Instant end);

    List<LinkClick> findByLink_Id(UUID linkId);

    long countByLink_Id(UUID linkId);
}
