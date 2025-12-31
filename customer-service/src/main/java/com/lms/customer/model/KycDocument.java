package com.lms.customer.model;

import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.model.enums.KycType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "kyc_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycDocument {

    @Id
    private String documentId;

    private String customerId;

    private KycType type;          // AADHAAR, PAN, etc.
    private KycStatus status;      // PENDING, APPROVED, REJECTED

    private String documentNumber; // masked value if needed
    private LocalDate expiryDate;

    // Admin audit fields
    private String verifiedBy;
    private LocalDateTime verifiedAt;
    private String remarks;

    // Metadata
    private LocalDateTime uploadedAt;
}
