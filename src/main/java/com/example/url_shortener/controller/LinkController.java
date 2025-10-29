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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Tag(name = "Links", description = "Gerenciamento de links encurtados e suas estatísticas.")
public class LinkController {

    private final LinkService linkService;

    // ===========================================================
    @Operation(
            summary = "Criar novo link encurtado",
            description = "Gera uma nova URL encurtada com base na URL original informada. "
            + "Permite definir domínio, senha, data de expiração e limite de cliques.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Link criado com sucesso",
                        content = @Content(schema = @Schema(implementation = LinkResponse.class))),
                @ApiResponse(responseCode = "400", description = "Requisição inválida"),
                @ApiResponse(responseCode = "404", description = "Domínio não encontrado")
            }
    )
    @PostMapping
    public ResponseEntity<LinkResponse> createLink(@RequestBody LinkCreateRequest request) {
        return ResponseEntity.ok(linkService.createLink(request));
    }

    // ===========================================================
    @Operation(
            summary = "Listar links",
            description = "Retorna uma lista paginada de todos os links cadastrados."
    )
    @GetMapping
    public ResponseEntity<Page<LinkResponse>> listLinks(Pageable pageable) {
        return ResponseEntity.ok(linkService.listLinks(pageable));
    }

    // ===========================================================
    @Operation(
            summary = "Buscar link por ID",
            description = "Retorna os detalhes completos de um link encurtado pelo seu identificador único (UUID).",
            responses = {
                @ApiResponse(responseCode = "200", description = "Link encontrado",
                        content = @Content(schema = @Schema(implementation = LinkResponse.class))),
                @ApiResponse(responseCode = "404", description = "Link não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<LinkResponse> getLinkById(
            @Parameter(description = "ID único do link", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id) {
        return ResponseEntity.ok(linkService.getLinkById(id));
    }

    // ===========================================================
    @Operation(
            summary = "Atualizar link",
            description = "Permite alterar a URL original, data de expiração, limite de cliques ou status de um link encurtado."
    )
    @PutMapping("/{id}")
    public ResponseEntity<LinkResponse> updateLink(
            @Parameter(description = "ID único do link a ser atualizado") @PathVariable UUID id,
            @RequestBody LinkUpdateRequest request
    ) {
        return ResponseEntity.ok(linkService.updateLink(id, request));
    }

    // ===========================================================
    @Operation(
            summary = "Excluir (desativar) link",
            description = "Desativa um link encurtado (soft delete). O link deixa de redirecionar, mas é mantido no histórico.",
            responses = {
                @ApiResponse(responseCode = "204", description = "Link desativado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Link não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(
            @Parameter(description = "ID do link a ser desativado") @PathVariable UUID id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }

    // ===========================================================
    @Operation(
            summary = "Obter estatísticas do link",
            description = "Retorna estatísticas básicas, como número total de cliques e data do último acesso.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso",
                        content = @Content(schema = @Schema(implementation = LinkStatsResponse.class))),
                @ApiResponse(responseCode = "404", description = "Link não encontrado")
            }
    )
    @GetMapping("/{id}/stats")
    public ResponseEntity<LinkStatsResponse> getStats(
            @Parameter(description = "ID do link para o qual buscar estatísticas") @PathVariable UUID id) {
        return ResponseEntity.ok(linkService.getLinkStats(id));
    }
}
