package com.lms.customer.dto;

import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.model.enums.KycType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycDocumentResponse {

    private String id;

    private KycType type;

    private KycStatus status;

    private String remarks;

    private LocalDateTime uploadedAt;

    private LocalDateTime verifiedAt;
}
