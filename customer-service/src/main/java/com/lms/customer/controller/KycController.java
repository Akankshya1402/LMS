package com.lms.customer.controller;

import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class KycController {

    private final KycService kycService;

    // =========================
    // CUSTOMER: UPLOAD KYC
    // =========================
    @PostMapping("/customers/me/kyc")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> uploadKyc(@RequestBody KycDocument document,
                                          Principal principal) {

        // In real app → customerId from JWT
        document.setCustomerId(principal.getName());

        kycService.uploadDocument(document);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // =========================
    // CUSTOMER: VIEW OWN KYC
    // =========================
    @GetMapping("/customers/me/kyc")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<KycDocument>> getMyKycDocuments(
            Principal principal) {

        List<KycDocument> documents =
                kycService.getMyDocuments(principal.getName());

        return ResponseEntity.ok(documents);
    }

    // =========================
    // ADMIN: VERIFY KYC
    // =========================
    @PutMapping("/admin/kyc/{documentId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> verifyKyc(
            @PathVariable String documentId,
            @RequestParam KycStatus status,
            @RequestParam String remarks) {

        kycService.verifyDocument(documentId, status, remarks);
        return ResponseEntity.ok().build();
    }
}
