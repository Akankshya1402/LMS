package com.lms.customer.service.impl;

import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.repository.KycDocumentRepository;
import com.lms.customer.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {

    private final KycDocumentRepository repository;

    @Override
    public void uploadDocument(KycDocument document) {

        // Default state when customer uploads
        document.setStatus(KycStatus.PENDING);
        document.setUploadedAt(LocalDateTime.now());

        repository.save(document);
    }

    @Override
    public List<KycDocument> getMyDocuments(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public void verifyDocument(String documentId,
                               KycStatus status,
                               String remarks) {

        KycDocument document = repository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("KYC document not found"));

        document.setStatus(status);
        document.setRemarks(remarks);
        document.setVerifiedAt(LocalDateTime.now());

        // Later replace with SecurityContext username
        document.setVerifiedBy("ADMIN");

        repository.save(document);
    }
}
