package com.lms.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class NotificationServiceApplicationTests {

    @MockBean
    private JavaMailSender javaMailSender; // 🔥 FIX

    @Test
    void contextLoads() {
    }
}
