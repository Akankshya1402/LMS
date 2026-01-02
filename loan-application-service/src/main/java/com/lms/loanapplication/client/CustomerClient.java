package com.lms.loanapplication.client;

import java.math.BigDecimal;

public interface CustomerClient {

    void validateCustomerForLoan(String customerId);

    Integer getCreditScore(String customerId);

    BigDecimal getMonthlyIncome(String customerId);

    BigDecimal getExistingEmiLiability(String customerId);

    void updateEmiLiability(String customerId, BigDecimal emiAmount);
}
