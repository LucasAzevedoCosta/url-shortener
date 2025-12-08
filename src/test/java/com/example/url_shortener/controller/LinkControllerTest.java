package com.example.url_shortener.controller;

import com.example.url_shortener.dto.link.LinkCreateRequest;
import com.example.url_shortener.dto.link.LinkResponse;
import com.example.url_shortener.service.LinkService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testCreateLink() throws Exception {
        LinkCreateRequest req = new LinkCreateRequest();
        req.setOriginalUrl("google.com");
        req.setDomainId("11111111-1111-1111-1111-111111111111");

        LinkResponse resp = new LinkResponse(
                "abc123",
                "http://localhost/abc123",
                "https://google.com",
                null,
                true
        );

        when(linkService.createLink(any())).thenReturn(resp);

        mockMvc.perform(post("/api/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"));
    }
}
