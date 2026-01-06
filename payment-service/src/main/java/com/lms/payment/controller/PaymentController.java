package com.lms.payment.controller;

import com.lms.payment.dto.PaymentRequest;
import com.lms.payment.dto.PaymentResponse;
import com.lms.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse payEmi(@Valid @RequestBody PaymentRequest request) {
        return service.makeEmiPayment(request);
    }

    @GetMapping("/loan/{loanId}")
    public List<PaymentResponse> byLoan(@PathVariable String loanId) {
        return service.getPaymentsByLoan(loanId);
    }

    @GetMapping("/customer/{customerId}")
    public List<PaymentResponse> byCustomer(@PathVariable String customerId) {
        return service.getPaymentsByCustomer(customerId);
    }
}