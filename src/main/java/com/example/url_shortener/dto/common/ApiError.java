package com.example.url_shortener.dto.common;

import java.time.Instant;
import java.util.List;

import com.example.url_shortener.exception.base.ApplicationException;

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

    @Builder.Default
    private List<ApiFieldError> details = List.of();

    public static ApiError fromException(ApplicationException ex, String path) {
        return ApiError.builder()
                .timestamp(Instant.now())
                .status(ex.getHttpStatus().value())
                .error(ex.getHttpStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(path)
                .code(ex.getErrorCode())
                .build();
    }

    public ApiError withDetails(List<ApiFieldError> details) {
        this.details = details;
        return this;
    }
}
