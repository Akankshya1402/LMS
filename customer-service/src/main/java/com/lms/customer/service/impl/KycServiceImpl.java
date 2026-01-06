package com.lms.customer.service.impl;

import com.lms.customer.dto.KycDocumentResponse;
import com.lms.customer.dto.KycUploadRequest;
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
    // CUSTOMER → UPLOAD DOCUMENT
    // =========================
    @Override
    public void uploadDocument(KycUploadRequest request, String customerId) {

        KycDocument document = new KycDocument();
        document.setCustomerId(customerId);
        document.setDocumentType(request.getDocumentType());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setStatus(KycStatus.PENDING);
        document.setUploadedAt(LocalDateTime.now());

        repository.save(document);

        customerService.updateKycStatus(customerId, KycStatus.PENDING);
    }

    // =========================
    // CUSTOMER → VIEW DOCUMENTS
    // =========================
    @Override
    public List<KycDocumentResponse> getMyDocuments(String customerId) {

        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // ADMIN → APPROVE DOCUMENT
    // =========================
    @Override
    public void approveDocument(String documentId, String remarks) {

        KycDocument document = repository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("KYC document not found"));

        document.setStatus(KycStatus.VERIFIED);
        document.setRemarks(remarks);
        document.setVerifiedBy("ADMIN");
        document.setVerifiedAt(LocalDateTime.now());

        repository.save(document);

        recalculateOverallKyc(document.getCustomerId());
    }

    // =========================
    // ADMIN → REJECT DOCUMENT
    // =========================
    @Override
    public void rejectDocument(String documentId, String remarks) {

        KycDocument document = repository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("KYC document not found"));

        document.setStatus(KycStatus.REJECTED);
        document.setRemarks(remarks);
        document.setVerifiedBy("ADMIN");
        document.setVerifiedAt(LocalDateTime.now());

        repository.save(document);

        recalculateOverallKyc(document.getCustomerId());
    }

    // =========================
    // INTERNAL HELPERS
    // =========================
    private void recalculateOverallKyc(String customerId) {

        List<KycDocument> documents = repository.findByCustomerId(customerId);

        if (documents.isEmpty()) {
            customerService.updateKycStatus(customerId, KycStatus.PENDING);
            return;
        }

        if (documents.stream().anyMatch(d -> d.getStatus() == KycStatus.REJECTED)) {
            customerService.updateKycStatus(customerId, KycStatus.REJECTED);
            return;
        }

        boolean allVerified = documents.stream()
                .allMatch(d -> d.getStatus() == KycStatus.VERIFIED);

        customerService.updateKycStatus(
                customerId,
                allVerified ? KycStatus.VERIFIED : KycStatus.PENDING
        );
    }

    private KycDocumentResponse toResponse(KycDocument document) {

        KycDocumentResponse dto = new KycDocumentResponse();
        dto.setId(document.getId());
        dto.setDocumentType(document.getDocumentType());
        dto.setStatus(document.getStatus());
        dto.setRemarks(document.getRemarks());
        dto.setUploadedAt(document.getUploadedAt());
        dto.setVerifiedAt(document.getVerifiedAt());

        return dto;
    }
}

