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

    @InjectMocks
    private KycServiceImpl service;

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
    }

    @Test
    void shouldReturnCustomerDocuments() {

        when(repository.findByCustomerId("C1"))
                .thenReturn(List.of(new KycDocument()));

        List<KycDocument> documents = service.getMyDocuments("C1");

        assertEquals(1, documents.size());
        verify(repository).findByCustomerId("C1");
    }

    @Test
    void shouldVerifyDocument() {

        KycDocument document = KycDocument.builder()
                .documentId("D1")
                .status(KycStatus.PENDING)
                .build();

        when(repository.findById("D1"))
                .thenReturn(Optional.of(document));

        service.verifyDocument("D1", KycStatus.APPROVED, "Verified");

        assertEquals(KycStatus.APPROVED, document.getStatus());
        assertEquals("Verified", document.getRemarks());
        assertNotNull(document.getVerifiedAt());

        verify(repository).save(document);
    }

    @Test
    void shouldThrowExceptionWhenDocumentNotFound() {

        when(repository.findById("X"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> service.verifyDocument("X", KycStatus.APPROVED, "OK"));

        assertEquals("KYC document not found", exception.getMessage());
    }
}
