package com.lms.payment.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    void nowUtcShouldReturnTimeInUtc() {

        LocalDateTime before = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime actual = TimeUtil.nowUtc();
        LocalDateTime after = LocalDateTime.now(ZoneOffset.UTC);

        assertNotNull(actual);
        assertFalse(actual.isBefore(before));
        assertFalse(actual.isAfter(after));
    }
}
