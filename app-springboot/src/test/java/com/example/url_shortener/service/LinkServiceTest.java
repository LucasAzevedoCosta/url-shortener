package com.example.url_shortener.service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.url_shortener.dto.link.LinkCreateRequest;
import com.example.url_shortener.dto.link.LinkUpdateRequest;
import com.example.url_shortener.entity.Domain;
import com.example.url_shortener.entity.Link;
import com.example.url_shortener.exception.domain.DomainNotFoundException;
import com.example.url_shortener.exception.link.LinkNotFoundException;
import com.example.url_shortener.helper.LinkHelpers;
import com.example.url_shortener.repository.DomainRepository;
import com.example.url_shortener.repository.LinkClickRepository;
import com.example.url_shortener.repository.LinkRepository;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private DomainRepository domainRepository;

    @Mock
    private LinkClickRepository linkClickRepository;

    @Mock
    private LinkHelpers linkHelpers;

    @InjectMocks
    private LinkService linkService;

    private Domain domain;

    @BeforeEach
    void setup() {
        domain = new Domain();
        domain.setId(UUID.randomUUID());
        domain.setActive(true);
    }

    @Test
    void testCreateLinkSuccess() {
        LinkCreateRequest req = new LinkCreateRequest();
        req.setDomainId(domain.getId().toString());
        req.setOriginalUrl("google.com");

        when(domainRepository.findById(domain.getId())).thenReturn(Optional.of(domain));
        when(linkHelpers.generateNextShortCode()).thenReturn("abc123");
        when(linkRepository.save(any(Link.class))).thenAnswer(i -> i.getArgument(0));

        var result = linkService.createLink(req);

        assertNotNull(result);
        assertEquals("abc123", result.getShortCode());
        assertEquals("https://google.com", result.getOriginalUrl());
        verify(linkRepository).save(any(Link.class));
    }

    @Test
    void testCreateLinkDomainNotFound() {
        LinkCreateRequest req = new LinkCreateRequest();
        req.setDomainId(UUID.randomUUID().toString());
        req.setOriginalUrl("google.com");

        when(domainRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DomainNotFoundException.class,
                () -> linkService.createLink(req));
    }

    @Test
    void testUpdateLink() {
        Link link = new Link();
        link.setShortCode("abc123");
        link.setOriginalUrl("https://old.com");

        LinkUpdateRequest req = new LinkUpdateRequest();
        req.setOriginalUrl("newsite.com");

        when(linkRepository.findById("abc123")).thenReturn(Optional.of(link));
        when(linkRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = linkService.updateLink("abc123", req);

        assertEquals("https://newsite.com", result.getOriginalUrl());
        verify(linkRepository).save(link);
    }

    @Test
    void testDeleteLink() {
        Link link = new Link();
        link.setShortCode("abc123");
        link.setIsActive(true);

        when(linkRepository.findById("abc123")).thenReturn(Optional.of(link));

        linkService.deleteLink("abc123");

        assertFalse(link.isActive());
        assertNotNull(link.getDeletedAt());
        verify(linkRepository).save(link);
    }

    @Test
    void testGetLinkNotFound() {
        when(linkRepository.findById("xyz")).thenReturn(Optional.empty());
        assertThrows(LinkNotFoundException.class, () -> linkService.getLinkByShortCode("xyz"));
    }
}
