package com.lms.customer.controller;

import com.lms.customer.dto.KycDocumentResponse;
import com.lms.customer.dto.KycUploadRequest;
import com.lms.customer.service.KycService;
import jakarta.validation.Valid;
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

    @PostMapping("/customers/me/kyc")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> uploadKyc(
            @RequestBody @Valid KycUploadRequest request,
            Principal principal) {

        kycService.uploadDocument(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/customers/me/kyc")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<KycDocumentResponse>> getMyKycDocuments(
            Principal principal) {

        return ResponseEntity.ok(
                kycService.getMyDocuments(principal.getName())
        );
    }

    @PutMapping("/admin/kyc/{documentId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveKyc(
            @PathVariable String documentId,
            @RequestParam(required = false) String remarks) {

        kycService.approveDocument(documentId, remarks);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/kyc/{documentId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectKyc(
            @PathVariable String documentId,
            @RequestParam String remarks) {

        kycService.rejectDocument(documentId, remarks);
        return ResponseEntity.ok().build();
    }
}

