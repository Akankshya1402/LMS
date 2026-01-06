package com.lms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}
