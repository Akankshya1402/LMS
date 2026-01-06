package com.lms.auth.controller;

import com.lms.auth.dto.AuthResponse;
import com.lms.auth.dto.ForgotPasswordRequest;
import com.lms.auth.dto.LoginRequest;
import com.lms.auth.dto.MessageResponse;
import com.lms.auth.dto.RegisterRequest;
import com.lms.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);
        return ResponseEntity.ok(
                new MessageResponse("User registered successfully")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(
                request.getUsername(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                new MessageResponse("Password updated successfully")
        );
    }
}

