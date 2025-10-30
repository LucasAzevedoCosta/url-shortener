package com.example.url_shortener.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.url_shortener.dto.DomainCreateRequest;
import com.example.url_shortener.dto.DomainResponse;
import com.example.url_shortener.dto.DomainUpdateRequest;
import com.example.url_shortener.service.DomainService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
@Tag(name = "Domains", description = "Gerenciamento de domínios disponíveis para encurtamento de links.")
public class DomainController {

    private final DomainService domainService;

    // ===========================================================
    @Operation(
            summary = "Criar novo domínio",
            description = "Registra um novo domínio que poderá ser usado para gerar URLs encurtadas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Domínio criado com sucesso",
                            content = @Content(schema = @Schema(implementation = DomainResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            }
    )
    @PostMapping
    public ResponseEntity<DomainResponse> createDomain(@RequestBody DomainCreateRequest request) {
        return ResponseEntity.ok(domainService.createDomain(request));
    }

    // ===========================================================
    @Operation(
            summary = "Listar domínios",
            description = "Retorna todos os domínios cadastrados (ativos e inativos).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de domínios retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<DomainResponse>> listDomains() {
        return ResponseEntity.ok(domainService.listDomains());
    }

    // ===========================================================
    @Operation(
            summary = "Buscar domínio por ID",
            description = "Retorna os detalhes de um domínio pelo seu identificador único (UUID).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Domínio encontrado",
                            content = @Content(schema = @Schema(implementation = DomainResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Domínio não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DomainResponse> getDomainById(
            @Parameter(description = "ID único do domínio", example = "a2b6c1d4-9f0e-4d35-9b2d-6f22f95f8e42")
            @PathVariable UUID id) {
        return ResponseEntity.ok(domainService.getDomainById(id));
    }

    // ===========================================================
    @Operation(
            summary = "Atualizar domínio",
            description = "Permite ativar ou desativar um domínio cadastrado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Domínio atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Domínio não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DomainResponse> updateDomain(
            @Parameter(description = "ID único do domínio a ser atualizado") @PathVariable UUID id,
            @RequestBody DomainUpdateRequest request
    ) {
        return ResponseEntity.ok(domainService.updateDomain(id, request));
    }

    // ===========================================================
    @Operation(
            summary = "Remover domínio (soft delete)",
            description = "Remove logicamente um domínio. Ele deixa de ser utilizado em novos links.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Domínio removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Domínio não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(
            @Parameter(description = "ID do domínio a ser removido") @PathVariable UUID id) {
        domainService.deleteDomain(id);
        return ResponseEntity.noContent().build();
    }
}
