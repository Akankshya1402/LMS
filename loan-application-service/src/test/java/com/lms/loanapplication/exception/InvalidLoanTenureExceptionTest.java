package com.lms.loanapplication.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidLoanTenureExceptionTest {

    @Test
    void shouldContainTenureValue() {
        Exception ex =
                new InvalidLoanTenureException(7);

        assertTrue(ex.getMessage().contains("7"));
    }
}
