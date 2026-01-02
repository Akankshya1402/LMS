package com.lms.customer.service.impl;

import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.repository.KycDocumentRepository;
import com.lms.customer.service.CustomerService;
import com.lms.customer.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {

    private final KycDocumentRepository repository;
    private final CustomerService customerService;

    // =========================
    // CUSTOMER: UPLOAD DOCUMENT
    // =========================
    @Override
    public void uploadDocument(KycDocument document) {

        document.setStatus(KycStatus.PENDING);
        document.setUploadedAt(LocalDateTime.now());

        repository.save(document);

        // Ensure customer is at least PENDING
        customerService.updateKycStatus(
                document.getCustomerId(),
                KycStatus.PENDING
        );
    }

    // =========================
    // CUSTOMER: VIEW DOCUMENTS
    // =========================
    @Override
    public List<KycDocument> getMyDocuments(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    // =========================
    // ADMIN: APPROVE DOCUMENT
    // =========================
    @Override
    public void approveDocument(String documentId, String remarks) {

        KycDocument document = repository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("KYC document not found"));

        document.setStatus(KycStatus.VERIFIED);
        document.setRemarks(remarks);
        document.setVerifiedBy("ADMIN");
        document.setVerifiedAt(LocalDateTime.now());

        repository.save(document);

        recalculateOverallKyc(document.getCustomerId());
    }

    // =========================
    // ADMIN: REJECT DOCUMENT
    // =========================
    @Override
    public void rejectDocument(String documentId, String remarks) {

        KycDocument document = repository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("KYC document not found"));

        document.setStatus(KycStatus.REJECTED);
        document.setRemarks(remarks);
        document.setVerifiedBy("ADMIN");
        document.setVerifiedAt(LocalDateTime.now());

        repository.save(document);

        recalculateOverallKyc(document.getCustomerId());
    }

    // =========================
    // INTERNAL: RECALCULATE KYC
    // =========================
    private void recalculateOverallKyc(String customerId) {

        List<KycDocument> documents =
                repository.findByCustomerId(customerId);

        boolean anyRejected = documents.stream()
                .anyMatch(d -> d.getStatus() == KycStatus.REJECTED);

        boolean allVerified = documents.stream()
                .allMatch(d -> d.getStatus() == KycStatus.VERIFIED);

        if (anyRejected) {
            customerService.updateKycStatus(customerId, KycStatus.REJECTED);
        } else if (allVerified && !documents.isEmpty()) {
            customerService.updateKycStatus(customerId, KycStatus.VERIFIED);
        } else {
            customerService.updateKycStatus(customerId, KycStatus.PENDING);
        }
    }
}
