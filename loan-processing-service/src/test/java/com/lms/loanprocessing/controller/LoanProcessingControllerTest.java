package com.lms.loanprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.loanprocessing.dto.EmiPaymentRequest;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.service.LoanProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanProcessingController.class)
class LoanProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanProcessingService service;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // GET LOAN (CUSTOMER / ADMIN)
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_CUSTOMER")
    void shouldReturnLoanDetails() throws Exception {

        Loan loan = Loan.builder()
                .loanId("L1")
                .principal(BigDecimal.valueOf(100000))
                .emiAmount(BigDecimal.valueOf(9000))
                .status(LoanStatus.ACTIVE)
                .build();

        when(service.getLoan("L1")).thenReturn(loan);

        mockMvc.perform(get("/loans/L1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value("L1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    // =========================
    // PAY EMI (CUSTOMER)
    // =========================
    @Test
    @WithMockUser(authorities = "ROLE_CUSTOMER")
    void shouldPayEmi() throws Exception {

        EmiPaymentRequest request = new EmiPaymentRequest();
        request.setLoanId("L1");
        request.setEmiNumber(1);

        doNothing().when(service)
                .recordEmiPayment("L1", 1);

        mockMvc.perform(post("/loans/emi/pay")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // =========================
    // SECURITY: UNAUTHORIZED
    // =========================
    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/loans/L1"))
                .andExpect(status().isUnauthorized());
    }
}
