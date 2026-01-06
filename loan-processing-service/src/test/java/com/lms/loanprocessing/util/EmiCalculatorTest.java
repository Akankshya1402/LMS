package com.lms.loanprocessing.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmiCalculatorTest {

    @Test
    void shouldCalculateCorrectEmi() {

        BigDecimal principal = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(10);
        int months = 12;

        BigDecimal emi =
                EmiCalculator.calculate(principal, rate, months);

        assertNotNull(emi);
        assertTrue(emi.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldHandleZeroInterest() {

        BigDecimal emi =
                EmiCalculator.calculate(
                        BigDecimal.valueOf(120000),
                        BigDecimal.ZERO,
                        12);

        assertNotNull(emi);
    }
}
