package com.lms.customer.model;

import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.model.enums.KycType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "kyc_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycDocument {

    @Id
    private String id;                // MongoDB document id

    private String customerId;        // From JWT / principal
    private KycType type;             // AADHAAR, PAN, ADDRESS_PROOF

    private KycStatus status;         // PENDING | VERIFIED | REJECTED

    private String documentNumber;    // Masked (e.g. XXXX1234)

    // Upload metadata
    private LocalDateTime uploadedAt;

    // Admin audit fields
    private String verifiedBy;
    private LocalDateTime verifiedAt;
    private String remarks;
}
