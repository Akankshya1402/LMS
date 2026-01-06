package com.lms.customer.service;

import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.model.enums.KycType;
import com.lms.customer.repository.KycDocumentRepository;
import com.lms.customer.service.impl.KycServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KycServiceTest {

    @Mock
    private KycDocumentRepository repository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private KycServiceImpl service;

    // =========================
    // CUSTOMER: UPLOAD DOCUMENT
    // =========================
    @Test
    void shouldUploadDocument() {

        KycDocument document = KycDocument.builder()
                .customerId("C1")
                .type(KycType.PAN)
                .build();

        service.uploadDocument(document);

        assertEquals(KycStatus.PENDING, document.getStatus());
        assertNotNull(document.getUploadedAt());

        verify(repository).save(document);
        verify(customerService)
                .updateKycStatus("C1", KycStatus.PENDING);
    }

    // =========================
    // CUSTOMER: VIEW DOCUMENTS
    // =========================
    @Test
    void shouldReturnCustomerDocuments() {

        when(repository.findByCustomerId("C1"))
                .thenReturn(List.of(new KycDocument()));

        List<KycDocument> documents = service.getMyDocuments("C1");

        assertEquals(1, documents.size());
        verify(repository).findByCustomerId("C1");
    }

    // =========================
    // ADMIN: APPROVE DOCUMENT
    // =========================
    @Test
    void shouldApproveDocument() {

        KycDocument document = KycDocument.builder()
                .id("D1")
                .customerId("C1")
                .status(KycStatus.VERIFIED)
                .build();

        when(repository.findById("D1"))
                .thenReturn(Optional.of(document));

        when(repository.findByCustomerId("C1"))
                .thenReturn(List.of(document));

        service.approveDocument("D1", "OK");

        verify(customerService)
                .updateKycStatus("C1", KycStatus.VERIFIED);
    }


    // =========================
    // ADMIN: REJECT DOCUMENT
    // =========================
    @Test
    void shouldRejectDocument() {

        KycDocument document = KycDocument.builder()
                .id("D1")
                .customerId("C1")
                .status(KycStatus.REJECTED)
                .build();

        when(repository.findById("D1"))
                .thenReturn(Optional.of(document));

        when(repository.findByCustomerId("C1"))
                .thenReturn(List.of(document));

        service.rejectDocument("D1", "Invalid");

        verify(customerService)
                .updateKycStatus("C1", KycStatus.REJECTED);
    }


    // =========================
    // ERROR: DOCUMENT NOT FOUND
    // =========================
    @Test
    void shouldThrowExceptionWhenDocumentNotFound() {

        when(repository.findById("X"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.approveDocument("X", "OK")
        );

        assertEquals("KYC document not found", exception.getMessage());
    }
}
