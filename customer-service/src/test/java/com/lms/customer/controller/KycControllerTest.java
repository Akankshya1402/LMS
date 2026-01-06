package com.lms.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.customer.model.KycDocument;
import com.lms.customer.model.enums.KycType;
import com.lms.customer.service.KycService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = KycController.class,
        excludeAutoConfiguration = {
                org.springframework.cloud.config.client.ConfigClientAutoConfiguration.class
        }
)
@ActiveProfiles("test")
class KycControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KycService kycService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldUploadKycDocument() throws Exception {

        KycDocument document = KycDocument.builder()
                .type(KycType.AADHAAR)
                .documentNumber("XXXX1234")
                .build();

        doNothing().when(kycService).uploadDocument(any());

        mockMvc.perform(post("/api/customers/me/kyc")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnCustomerKycDocuments() throws Exception {

        when(kycService.getMyDocuments(any()))
                .thenReturn(List.of(new KycDocument()));

        mockMvc.perform(get("/api/customers/me/kyc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldApproveKycDocument() throws Exception {

        doNothing().when(kycService).approveDocument("D1", "OK");

        mockMvc.perform(put("/api/admin/kyc/D1/approve")
                        .with(csrf())
                        .param("remarks", "OK"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectKycDocument() throws Exception {

        doNothing().when(kycService).rejectDocument("D1", "Invalid");

        mockMvc.perform(put("/api/admin/kyc/D1/reject")
                        .with(csrf())
                        .param("remarks", "Invalid"))
                .andExpect(status().isOk());
    }
}

