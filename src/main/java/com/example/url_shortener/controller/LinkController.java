package com.example.url_shortener.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.dto.LinkCreateRequest;
import com.example.url_shortener.dto.LinkResponse;
import com.example.url_shortener.dto.LinkStatsResponse;
import com.example.url_shortener.dto.LinkUpdateRequest;
import com.example.url_shortener.service.LinkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<LinkResponse> createLink(@RequestBody LinkCreateRequest request) {
        return ResponseEntity.ok(linkService.createLink(request));
    }

    @GetMapping
    public ResponseEntity<Page<LinkResponse>> listLinks(Pageable pageable) {
        return ResponseEntity.ok(linkService.listLinks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkResponse> getLinkById(@PathVariable UUID id) {
        return ResponseEntity.ok(linkService.getLinkById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkResponse> updateLink(
            @PathVariable UUID id,
            @RequestBody LinkUpdateRequest request
    ) {
        return ResponseEntity.ok(linkService.updateLink(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable UUID id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<LinkStatsResponse> getStats(@PathVariable UUID id) {
        return ResponseEntity.ok(linkService.getLinkStats(id));
    }
}
