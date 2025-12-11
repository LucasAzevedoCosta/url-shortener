package com.example.url_shortener.exception.handler;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.url_shortener.dto.common.ApiError;
import com.example.url_shortener.dto.common.ApiFieldError;
import com.example.url_shortener.exception.base.ApplicationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(ApplicationException ex, HttpServletRequest req) {

        ApiError error = ApiError.fromException(ex, req.getRequestURI());

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest req) {

        List<ApiFieldError> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new ApiFieldError(f.getField(), f.getDefaultMessage()))
                .toList();

        ApiError error = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Erro de validação nos campos enviados")
                .path(req.getRequestURI())
                .code("VALIDATION_ERROR")
                .details(details)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {

        ApiError error = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .code("INTERNAL_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
