package com.lms.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @PatchMapping("/customers/emi/reduce")
    void reduceEmiLiability(
            @RequestParam String customerId,
            @RequestParam BigDecimal amount
    );
}
