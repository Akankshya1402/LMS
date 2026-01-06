package com.lms.payment.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRefGeneratorTest {

    @Test
    void generateShouldReturnTxnPrefixedUniqueValue() {

        String ref1 = TransactionRefGenerator.generate();
        String ref2 = TransactionRefGenerator.generate();

        assertNotNull(ref1);
        assertTrue(ref1.startsWith("TXN-"));
        assertNotEquals(ref1, ref2);
    }
}
