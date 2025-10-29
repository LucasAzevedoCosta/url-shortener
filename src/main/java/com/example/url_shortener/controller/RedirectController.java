package com.example.url_shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.service.RedirectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "Redirect", description = "Lida com o redirecionamento de URLs encurtadas.")
public class RedirectController {

    @Autowired
    private RedirectService redirectService;

    @Operation(
            summary = "Redirecionar para URL original",
            description = "Recebe um código encurtado e redireciona o usuário para a URL original correspondente. "
            + "Também registra o clique para fins de estatística."
    )
    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(
            @Parameter(description = "Código curto gerado pelo encurtador", example = "abc123")
            @PathVariable String shortCode,
            @Parameter(description = "Host de onde a requisição foi feita")
            @RequestHeader("Host") String host,
            HttpServletRequest request
    ) {
        return redirectService.handleRedirect(host, shortCode, request);
    }
}
