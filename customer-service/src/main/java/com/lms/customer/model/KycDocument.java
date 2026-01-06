package com.lms.customer.model;

import com.lms.customer.model.enums.KycStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "kyc_documents")
public class KycDocument {

    @Id
    private String id;

    private String customerId;

    private String documentType;   // 🔥 MUST be camelCase

    private String documentNumber;

    private KycStatus status;

    private String remarks;

    private String verifiedBy;

    private LocalDateTime uploadedAt;

    private LocalDateTime verifiedAt;
}
