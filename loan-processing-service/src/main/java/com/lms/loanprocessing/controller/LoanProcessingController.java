package com.lms.loanprocessing.controller;

import com.lms.loanprocessing.dto.EmiPaymentRequest;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.service.LoanProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanProcessingController {

    private final LoanProcessingService service;

    // =========================
    // CUSTOMER / ADMIN: GET LOAN DETAILS
    // =========================
    @GetMapping("/{loanId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<Loan> getLoan(
            @PathVariable String loanId) {

        return ResponseEntity.ok(service.getLoan(loanId));
    }

    // =========================
    // CUSTOMER: PAY EMI
    // =========================
    @PostMapping("/emi/pay")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> payEmi(
            @RequestBody EmiPaymentRequest request) {

        service.recordEmiPayment(
                request.getLoanId(),
                request.getEmiNumber());

        return ResponseEntity.ok().build();
    }
}
