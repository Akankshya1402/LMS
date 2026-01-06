package com.lms.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // BUSINESS EXCEPTIONS
    // =========================

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    // =========================
    // SECURITY EXCEPTIONS (CRITICAL)
    // =========================

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access is denied");
    }

    // =========================
    // VALIDATION ERRORS
    // =========================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // =========================
    // FALLBACK (REAL 500 ONLY)
    // =========================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong. Please try again.");
    }
}
