package com.lms.loanapplication.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // DOMAIN EXCEPTIONS
    // =========================
    @ExceptionHandler(LoanApplicationException.class)
    public ResponseEntity<Map<String, Object>>
    handleLoanApplicationException(LoanApplicationException ex) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // =========================
    // VALIDATION ERRORS
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(),
                                err.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    // =========================
    // FALLBACK (500)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleGeneric(Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
    }

    // =========================
    // UTIL
    // =========================
    private ResponseEntity<Map<String, Object>>
    buildResponse(HttpStatus status, String message) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}
