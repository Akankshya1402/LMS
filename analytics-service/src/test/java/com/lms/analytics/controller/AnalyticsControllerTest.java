package com.lms.analytics.controller;

import com.lms.analytics.config.SecurityConfig;
import com.lms.analytics.dto.response.DashboardResponse;
import com.lms.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    // =========================
    // ADMIN → SUCCESS (200)
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnDashboardForAdmin() throws Exception {

        DashboardResponse response = DashboardResponse.builder()
                .totalLoans(10)
                .approvedLoans(5)
                .pendingLoans(3)
                .rejectedLoans(2)
                .totalDisbursedAmount(BigDecimal.valueOf(500000))
                .activeCustomers(20)
                .totalEmiCollected(BigDecimal.valueOf(100000))
                .build();

        when(analyticsService.getDashboard()).thenReturn(response);

        mockMvc.perform(get("/analytics/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLoans").value(10))
                .andExpect(jsonPath("$.approvedLoans").value(5))
                .andExpect(jsonPath("$.activeCustomers").value(20));
    }

    // =========================
    // USER → FORBIDDEN (403)
    // =========================
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn403ForNonAdminUser() throws Exception {

        mockMvc.perform(get("/analytics/dashboard"))
                .andExpect(status().isForbidden());
    }

    // =========================
    // NO AUTH → UNAUTHORIZED (401)
    // =========================
    @Test
    void shouldReturn401ForUnauthenticatedUser() throws Exception {

        mockMvc.perform(get("/analytics/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}

