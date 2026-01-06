package com.lms.loanapplication.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customer-service", path = "/customers")
public interface CustomerClient {

    @GetMapping("/{customerId}/validate")
    void validateCustomerForLoan(@PathVariable String customerId);
}
