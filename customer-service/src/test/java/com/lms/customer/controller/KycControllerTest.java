package com.lms.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.customer.dto.KycUploadRequest;
import com.lms.customer.model.KycDocument;
import com.lms.customer.service.KycService;
import com.lms.customer.dto.KycDocumentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    // CUSTOMER → UPLOAD KYC
    // =========================
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldUploadKycDocument() throws Exception {

        KycUploadRequest request = new KycUploadRequest();
        request.setDocumentType("AADHAAR");
        request.setDocumentNumber("XXXX1234");

        doNothing().when(kycService)
                .uploadDocument(any(KycUploadRequest.class), anyString());

        mockMvc.perform(post("/api/customers/me/kyc")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // =========================
    // CUSTOMER → VIEW DOCUMENTS
    // =========================
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnCustomerKycDocuments() throws Exception {

    	when(kycService.getMyDocuments(anyString()))
        .thenReturn(List.of(new KycDocumentResponse()));


        mockMvc.perform(get("/api/customers/me/kyc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // =========================
    // ADMIN → APPROVE KYC
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldApproveKycDocument() throws Exception {

        doNothing().when(kycService)
                .approveDocument("D1", "OK");

        mockMvc.perform(put("/api/admin/kyc/D1/approve")
                        .with(csrf())
                        .param("remarks", "OK"))
                .andExpect(status().isOk());
    }

    // =========================
    // ADMIN → REJECT KYC
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectKycDocument() throws Exception {

        doNothing().when(kycService)
                .rejectDocument("D1", "Invalid");

        mockMvc.perform(put("/api/admin/kyc/D1/reject")
                        .with(csrf())
                        .param("remarks", "Invalid"))
                .andExpect(status().isOk());
    }
}

