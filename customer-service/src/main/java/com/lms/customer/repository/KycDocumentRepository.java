package com.lms.customer.repository;

import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.model.enums.KycType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface KycDocumentRepository
        extends MongoRepository<KycDocument, String> {

    // All documents of a customer
    List<KycDocument> findByCustomerId(String customerId);

    // Find a specific document type for a customer (PAN, AADHAAR, etc.)
    Optional<KycDocument> findByCustomerIdAndType(
            String customerId,
            KycType type
    );

    // Find documents by status (used for verification checks)
    List<KycDocument> findByCustomerIdAndStatus(
            String customerId,
            KycStatus status
    );
}
