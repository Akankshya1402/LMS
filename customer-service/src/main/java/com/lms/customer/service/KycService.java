package com.lms.customer.service;

import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;

import java.util.List;

public interface KycService {

    // Customer uploads a document
    void uploadDocument(KycDocument document);

    // Customer views own documents
    List<KycDocument> getMyDocuments(String customerId);

    // Admin verifies document
    void verifyDocument(String documentId, KycStatus status, String remarks);
}
