package com.lms.customer.repository;

import com.lms.customer.model.KycDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KycDocumentRepository
        extends MongoRepository<KycDocument, String> {

    List<KycDocument> findByCustomerId(String customerId);
}
