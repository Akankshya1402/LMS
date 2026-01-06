package com.lms.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loan-processing-service")
public interface LoanProcessingClient {

    @PostMapping("/loans/emi/pay")
    void recordEmiPayment(
            @RequestParam String loanId,
            @RequestParam Integer emiNumber
    );
}
