package com.example.url_shortener.dto.common;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    @Builder.Default
    private Instant timestamp = Instant.now();

    private int status;

    private String error;

    private String message;

    private String path;

    private String code;

    private List<ApiFieldError> details;
}
