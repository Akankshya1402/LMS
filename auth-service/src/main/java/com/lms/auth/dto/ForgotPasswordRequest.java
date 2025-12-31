package com.lms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
//hi
@Getter
@Setter
public class ForgotPasswordRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
