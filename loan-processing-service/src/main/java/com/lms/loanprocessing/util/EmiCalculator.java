package com.lms.loanprocessing.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class EmiCalculator {

    private EmiCalculator() {
        // utility class
    }

    public static BigDecimal calculate(BigDecimal principal,
                                       BigDecimal annualRate,
                                       int months) {

        if (principal == null || annualRate == null) {
            throw new IllegalArgumentException("Principal and rate must not be null");
        }

        if (months <= 0) {
            throw new IllegalArgumentException("Months must be greater than zero");
        }

        // ✅ ZERO INTEREST CASE (this fixes your test failure)
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(
                    BigDecimal.valueOf(months),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRPowerN =
                monthlyRate.add(BigDecimal.ONE).pow(months);

        return principal
                .multiply(monthlyRate)
                .multiply(onePlusRPowerN)
                .divide(
                        onePlusRPowerN.subtract(BigDecimal.ONE),
                        2,
                        RoundingMode.HALF_UP
                );
    }
}
