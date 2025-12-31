package com.lms.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.model.enums.KycType;
import com.lms.customer.service.KycService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KycController.class)
class KycControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KycService kycService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // CUSTOMER: UPLOAD KYC
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_CUSTOMER")
    void shouldUploadKycDocument() throws Exception {

        KycDocument document = KycDocument.builder()
                .type(KycType.AADHAAR)
                .documentNumber("XXXX-XXXX-1234")
                .build();

        doNothing().when(kycService).uploadDocument(any(KycDocument.class));

        mockMvc.perform(post("/customers/me/kyc")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated());
    }

    // =========================
    // CUSTOMER: VIEW KYC
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_CUSTOMER")
    void shouldReturnCustomerKycDocuments() throws Exception {

        when(kycService.getMyDocuments(any()))
                .thenReturn(List.of(new KycDocument()));

        mockMvc.perform(get("/customers/me/kyc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // =========================
    // ADMIN: VERIFY KYC
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldVerifyKycDocument() throws Exception {

        doNothing().when(kycService)
                .verifyDocument("D1", KycStatus.APPROVED, "OK");

        mockMvc.perform(put("/admin/kyc/D1/verify")
                        .with(csrf())
                        .param("status", "APPROVED")
                        .param("remarks", "OK"))
                .andExpect(status().isOk());
    }

    // =========================
    // SECURITY: UNAUTHORIZED
    // =========================
    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/customers/me/kyc"))
                .andExpect(status().isUnauthorized());
    }
}
