package com.lms.loanprocessing.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            LoanNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 404,
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 500,
                        "message", "Internal server error"));
    }
}

