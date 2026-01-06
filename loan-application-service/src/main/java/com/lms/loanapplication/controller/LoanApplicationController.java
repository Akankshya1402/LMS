package com.lms.loanapplication.controller;

import com.lms.loanapplication.dto.*;
import com.lms.loanapplication.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> apply(
            @Valid @RequestBody LoanApplicationRequest request) {

        // Fixed user for JMETER
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.apply("jmeter-user", request));
    }

    @GetMapping("/me")
    public List<LoanApplicationResponse> myApplications() {
        return service.getMyApplications("jmeter-user");
    }
}



