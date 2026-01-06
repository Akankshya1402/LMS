package com.lms.loanprocessing.controller;

import com.lms.loanprocessing.dto.EmiOverviewResponse;
import com.lms.loanprocessing.exception.LoanNotFoundException;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.repository.LoanRepository;
import com.lms.loanprocessing.service.LoanServicingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanProcessingController {

    private final LoanRepository loanRepository;
    private final LoanServicingService servicingService;

    // =========================
    // GET LOAN DETAILS
    // =========================
    @GetMapping("/{loanId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<Loan> getLoan(
            @PathVariable String loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new LoanNotFoundException("Loan not found"));

        return ResponseEntity.ok(loan);
    }

    // =========================
    // EMI PAID / PENDING / OVERDUE OVERVIEW
    // =========================
    @GetMapping("/{loanId}/emi-overview")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<EmiOverviewResponse> getEmiOverview(
            @PathVariable String loanId) {

        return ResponseEntity.ok(
                servicingService.getEmiOverview(loanId)
        );
    }
}
