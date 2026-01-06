package com.lms.loanapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;
import com.lms.loanapplication.exception.GlobalExceptionHandler;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.model.enums.LoanType;
import com.lms.loanapplication.service.LoanApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanApplicationController.class)
@Import(GlobalExceptionHandler.class)
class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user1", roles = "CUSTOMER")
    void shouldApplyForLoan() throws Exception {

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setLoanType(LoanType.PERSONAL);
        request.setLoanAmount(BigDecimal.valueOf(100000));
        request.setTenureMonths(12);

        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setApplicationId("APP1");
        response.setStatus(ApplicationStatus.SUBMITTED);
        response.setAppliedAt(LocalDateTime.now());

        when(service.apply(any(), any())).thenReturn(response);

        mockMvc.perform(post("/loan-applications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationId").value("APP1"))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "CUSTOMER")
    void shouldReturnMyApplications() throws Exception {

        when(service.getMyApplications(any()))
                .thenReturn(List.of(new LoanApplicationResponse()));

        mockMvc.perform(get("/loan-applications/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
