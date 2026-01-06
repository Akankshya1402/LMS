package com.lms.loanapplication.exception;

import com.lms.loanapplication.controller.LoanApplicationController;
import com.lms.loanapplication.service.LoanApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanApplicationController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationService service;

    @Test
    @WithMockUser
    void shouldHandleDomainException() throws Exception {

        when(service.apply(any(), any()))
                .thenThrow(new KycNotVerifiedException());

        mockMvc.perform(post("/loan-applications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "loanType": "PERSONAL",
                              "loanAmount": 100000,
                              "tenureMonths": 12
                            }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("KYC not verified. Cannot apply for loan."));
    }

    @Test
    @WithMockUser
    void shouldHandleGenericException() throws Exception {

        when(service.apply(any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/loan-applications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "loanType": "PERSONAL",
                              "loanAmount": 100000,
                              "tenureMonths": 12
                            }
                        """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Unexpected error"));
    }
}

