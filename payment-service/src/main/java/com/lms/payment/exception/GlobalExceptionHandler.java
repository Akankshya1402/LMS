package com.lms.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // VALIDATION ERRORS (400)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    // =========================
    // MALFORMED JSON / EMPTY BODY (400)
    // =========================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMalformedJson() {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid or empty request body");

        return ResponseEntity.badRequest().body(error);
    }

    // =========================
    // BUSINESS EXCEPTION (409)
    // =========================
    @ExceptionHandler(PaymentAlreadyProcessedException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyProcessed(
            PaymentAlreadyProcessedException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // =========================
    // FALLBACK (500)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.internalServerError().body(error);
    }
}
