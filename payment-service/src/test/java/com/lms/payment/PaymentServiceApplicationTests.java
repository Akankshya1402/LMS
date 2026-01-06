package com.lms.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")   // 🔴 THIS IS THE KEY LINE
class PaymentServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
