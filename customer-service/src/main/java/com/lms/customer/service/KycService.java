package com.lms.customer.service;

import com.lms.customer.model.KycDocument;

import java.util.List;

public interface KycService {

    // =========================
    // CUSTOMER ACTIONS
    // =========================

    // Upload a KYC document (always goes to PENDING)
    void uploadDocument(KycDocument document);

    // View own uploaded documents
    List<KycDocument> getMyDocuments(String customerId);

    // =========================
    // ADMIN ACTIONS
    // =========================

    // Approve a KYC document
    void approveDocument(String documentId, String remarks);

    // Reject a KYC document
    void rejectDocument(String documentId, String remarks);
}
