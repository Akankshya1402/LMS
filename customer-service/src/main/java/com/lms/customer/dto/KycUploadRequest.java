package com.lms.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KycUploadRequest {

    @NotBlank
    private String documentType;

    @NotBlank
    private String documentNumber;
}
