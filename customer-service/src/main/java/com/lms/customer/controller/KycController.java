package com.lms.customer.controller;

import com.lms.customer.model.KycDocument;
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
@RequestMapping("/api")
public class KycController {

    private final KycService kycService;

    // =========================
    // CUSTOMER → UPLOAD KYC DOCUMENT
    // =========================
    @PostMapping("/customers/me/kyc")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> uploadKyc(
            @RequestBody KycDocument document,
            Principal principal) {

        // Server-controlled fields
        document.setCustomerId(principal.getName());

        kycService.uploadDocument(document);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // =========================
    // CUSTOMER → VIEW OWN KYC DOCUMENTS
    // =========================
    @GetMapping("/customers/me/kyc")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<KycDocument>> getMyKycDocuments(
            Principal principal) {

        return ResponseEntity.ok(
                kycService.getMyDocuments(principal.getName())
        );
    }

    // =========================
    // ADMIN → APPROVE KYC DOCUMENT
    // =========================
    @PutMapping("/admin/kyc/{documentId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveKyc(
            @PathVariable String documentId,
            @RequestParam(required = false) String remarks) {

        kycService.approveDocument(documentId, remarks);
        return ResponseEntity.ok().build();
    }

    // =========================
    // ADMIN → REJECT KYC DOCUMENT
    // =========================
    @PutMapping("/admin/kyc/{documentId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectKyc(
            @PathVariable String documentId,
            @RequestParam String remarks) {

        kycService.rejectDocument(documentId, remarks);
        return ResponseEntity.ok().build();
    }
}

