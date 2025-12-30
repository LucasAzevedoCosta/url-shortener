package com.example.url_shortener.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.url_shortener.entity.Link;

@SpringBootTest
@ActiveProfiles("test")
class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Test
    void testSaveAndFind() {
        Link link = new Link();
        link.setShortCode("xyz789");
        link.setOriginalUrl("https://example.com");

        linkRepository.save(link);

        Optional<Link> found = linkRepository.findById("xyz789");

        assertTrue(found.isPresent());
        assertEquals("https://example.com", found.get().getOriginalUrl());
    }
}
