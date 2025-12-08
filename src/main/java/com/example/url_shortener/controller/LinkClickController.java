package com.example.url_shortener.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.dto.link.LinkClickResponse;
import com.example.url_shortener.dto.link.LinkStatsResponse;
import com.example.url_shortener.service.LinkClickService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/links/{linkId}")
@RequiredArgsConstructor
@Tag(name = "Link Clicks", description = "Gerenciamento e estatísticas de cliques em links encurtados.")
public class LinkClickController {

    private final LinkClickService linkClickService;

    // ===========================================================
    @Operation(
            summary = "Listar cliques de um link",
            description = "Retorna todos os cliques registrados para um link específico. Pode ser filtrado por período de tempo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de cliques retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = LinkClickResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Link não encontrado")
            }
    )
    @GetMapping("/clicks")
    public ResponseEntity<List<LinkClickResponse>> getClicks(
            @Parameter(description = "Identificador único (shortCode) do link encurtado", example = "xYz12A")
            @PathVariable String linkId,

            @Parameter(description = "Data/hora inicial para filtro de cliques", example = "2025-01-01T00:00:00Z")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,

            @Parameter(description = "Data/hora final para filtro de cliques", example = "2025-02-01T00:00:00Z")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ) {
        return ResponseEntity.ok(linkClickService.listClicks(linkId, start, end));
    }

    // ===========================================================
    @Operation(
            summary = "Obter estatísticas de cliques",
            description = "Retorna estatísticas agregadas do link, como total de cliques, visitantes únicos, último clique e top países.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso",
                            content = @Content(schema = @Schema(implementation = LinkStatsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Link não encontrado")
            }
    )
    @GetMapping("/stats")
    public ResponseEntity<LinkStatsResponse> getStats(
            @Parameter(description = "Identificador único (shortCode) do link encurtado", example = "xYz12A")
            @PathVariable String linkId
    ) {
        return ResponseEntity.ok(linkClickService.getLinkStats(linkId));
    }
}
