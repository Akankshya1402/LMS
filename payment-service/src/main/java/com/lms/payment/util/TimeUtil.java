package com.lms.payment.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class TimeUtil {

    private TimeUtil() {
        // utility class
    }

    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}

