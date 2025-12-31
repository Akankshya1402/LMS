package com.lms.loanapplication.controller;

import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;
import com.lms.loanapplication.service.LoanApplicationService;
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
@RequestMapping("/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    // CUSTOMER
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoanApplicationResponse> apply(
            @Valid @RequestBody LoanApplicationRequest request,
            Principal principal) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.apply(principal.getName(), request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<LoanApplicationResponse> myApplications(
            Principal principal) {

        return service.getMyApplications(principal.getName());
    }

    // ADMIN / LOAN OFFICER
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN','LOAN_OFFICER')")
    public List<LoanApplicationResponse> pending() {
        return service.getPendingApplications();
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN','LOAN_OFFICER')")
    public LoanApplicationResponse review(
            @PathVariable String id,
            @RequestParam boolean approved,
            @RequestParam String remarks,
            Principal principal) {

        return service.review(id, approved, remarks, principal.getName());
    }
}
