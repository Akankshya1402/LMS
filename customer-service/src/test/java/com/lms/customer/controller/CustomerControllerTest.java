package com.lms.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.customer.dto.CustomerRequest;
import com.lms.customer.dto.CustomerResponse;
import com.lms.customer.model.enums.AccountStatus;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // CREATE CUSTOMER (ADMIN)
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldCreateCustomer() throws Exception {

        CustomerRequest request = CustomerRequest.builder()
                .fullName("John Doe")
                .email("john@test.com")
                .mobile("9876543210")
                .monthlyIncome(BigDecimal.valueOf(50000))
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .customerId("1")
                .fullName("John Doe")
                .email("john@test.com")
                .mobile("9876543210")
                .monthlyIncome(BigDecimal.valueOf(50000))
                .accountStatus(AccountStatus.ACTIVE)
                .kycStatus(KycStatus.NOT_SUBMITTED)
                .build();

        when(service.create(any(CustomerRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.kycStatus").value("NOT_SUBMITTED"));
    }

    // =========================
    // GET CUSTOMER BY ID (ADMIN)
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldGetCustomerById() throws Exception {

        CustomerResponse response = CustomerResponse.builder()
                .customerId("1")
                .fullName("Bob Smith")
                .email("bob@test.com")
                .mobile("9123456789")
                .monthlyIncome(BigDecimal.valueOf(70000))
                .accountStatus(AccountStatus.ACTIVE)
                .kycStatus(KycStatus.APPROVED)
                .build();

        when(service.getById("1")).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("1"))
                .andExpect(jsonPath("$.email").value("bob@test.com"))
                .andExpect(jsonPath("$.kycStatus").value("APPROVED"));
    }

    // =========================
    // SECURITY: NOT AUTHENTICATED
    // =========================
    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isUnauthorized());
    }

    // =========================
    // VALIDATION FAILURE
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldReturn400WhenValidationFails() throws Exception {

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

