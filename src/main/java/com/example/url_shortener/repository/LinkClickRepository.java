package com.example.url_shortener.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.url_shortener.entity.Link;
import com.example.url_shortener.entity.LinkClick;

@Repository
public interface LinkClickRepository extends JpaRepository<LinkClick, Long> {

    List<LinkClick> findByLink_ShortCodeAndClickedAtBetween(String shortCode, Instant start, Instant end);

    List<LinkClick> findByLink_ShortCode(String shortCode);

    long countByLink_ShortCode(String shortCode);

    List<LinkClick> findByLink(Link link);

    List<LinkClick> findByLinkAndClickedAtBetween(Link link, Instant start, Instant end);

    long countByLink(Link link);

    @Query("SELECT COUNT(DISTINCT c.ip) FROM LinkClick c WHERE c.link = :link")
    long countUniqueVisitorsByLink(Link link);

    @Query("SELECT c.country AS country, COUNT(c) AS clicks FROM LinkClick c WHERE c.link = :link GROUP BY c.country ORDER BY COUNT(c) DESC")
    List<Object[]> countClicksByCountry(Link link);
}
