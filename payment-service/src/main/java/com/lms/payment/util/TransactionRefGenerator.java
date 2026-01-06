package com.lms.payment.util;

import java.util.UUID;

public final class TransactionRefGenerator {

    private TransactionRefGenerator() {
        // utility class
    }

    public static String generate() {
        return "TXN-" + UUID.randomUUID();
    }
}
