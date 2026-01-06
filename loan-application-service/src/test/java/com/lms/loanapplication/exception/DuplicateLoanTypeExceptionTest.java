package com.lms.loanapplication.exception;

import com.lms.loanapplication.model.enums.LoanType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateLoanTypeExceptionTest {

    @Test
    void shouldIncludeLoanTypeInMessage() {
        Exception ex =
                new DuplicateLoanTypeException(LoanType.PERSONAL);

        assertTrue(ex.getMessage().contains("PERSONAL"));
    }
}
