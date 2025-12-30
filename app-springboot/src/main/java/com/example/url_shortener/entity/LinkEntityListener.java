package com.example.url_shortener.entity;

import java.time.Instant;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class LinkEntityListener {

    @PrePersist
    public void prePersist(Link link) {
        if (link.getCreatedAt() == null) {
            link.setCreatedAt(Instant.now());
        }
        if (link.getUpdatedAt() == null) {
            link.setUpdatedAt(Instant.now());
        }
    }

    @PreUpdate
    public void preUpdate(Link link) {
        link.setUpdatedAt(Instant.now());
    }

    @PreRemove
    public void preRemove(Link link) {
        link.setIsActive(false);
        link.setDeletedAt(Instant.now());
    }
}