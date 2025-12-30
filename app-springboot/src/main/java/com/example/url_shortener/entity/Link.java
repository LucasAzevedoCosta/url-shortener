package com.example.url_shortener.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(LinkEntityListener.class)
@Table(name = "links")
public class Link {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", referencedColumnName = "id")
    private Domain domain;

    @Id
    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private long clickCount = 0;

    @Column(nullable = false)
    private boolean isActive = true;

    private Instant expiresAt;

    private Long maxClicks;

    private String passwordHash;

    @Column(nullable = false)
    private String metadata = "{}";

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    private Instant deletedAt;

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}

