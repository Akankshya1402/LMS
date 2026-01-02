package com.lms.notification.controller;

import com.lms.notification.model.Notification;
import com.lms.notification.model.enums.NotificationStatus;
import com.lms.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationRepository repository;

    @Test
    void shouldReturnNotificationById() throws Exception {

        Notification notification = Notification.builder()
                .id("1")
                .recipient("user@test.com")
                .message("Loan Approved")
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(repository.findById("1"))
                .thenReturn(Optional.of(notification));

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipient").value("user@test.com"));
    }
}
