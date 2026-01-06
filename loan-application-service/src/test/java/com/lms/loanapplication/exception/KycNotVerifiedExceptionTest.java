package com.lms.loanapplication.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KycNotVerifiedExceptionTest {

    @Test
    void shouldHaveCorrectMessage() {
        Exception ex = new KycNotVerifiedException();
        assertEquals(
                "KYC not verified. Cannot apply for loan.",
                ex.getMessage()
        );
    }
}
