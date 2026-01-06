package com.lms.loanprocessing.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(
        name = "customer-service",
        path = "/customers"
)
public interface CustomerClient {

    // =========================
    // FETCH CUSTOMER PROFILE
    // =========================
    @GetMapping("/{customerId}/profile")
    CustomerProfile getProfile(
            @PathVariable("customerId") String customerId
    );

    // =========================
    // UPDATE EMI LIABILITY
    // =========================
    @PutMapping("/{customerId}/emi-liability")
    void updateEmiLiability(
            @PathVariable("customerId") String customerId,
            @RequestParam("emiAmount") BigDecimal emiAmount
    );

    // =========================
    // DTO
    // =========================
    @Data
    class CustomerProfile {

        private String customerId;
        private String email;
        private Integer creditScore;
        private BigDecimal monthlyIncome;
        private BigDecimal existingEmiLiability;
    }
}

