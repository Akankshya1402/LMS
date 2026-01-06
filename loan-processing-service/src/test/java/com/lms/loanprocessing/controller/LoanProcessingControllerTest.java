package com.lms.loanprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.loanprocessing.dto.EmiOverviewResponse;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.repository.LoanRepository;
import com.lms.loanprocessing.service.LoanServicingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanProcessingController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoanProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private LoanServicingService servicingService;

    @Test
    void shouldReturnLoanById() throws Exception {

        Loan loan = Loan.builder()
                .loanId("L1")
                .build();

        when(loanRepository.findById("L1"))
                .thenReturn(Optional.of(loan));

        mockMvc.perform(get("/loans/L1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnEmiOverview() throws Exception {

        when(servicingService.getEmiOverview("L1"))
                .thenReturn(EmiOverviewResponse.builder().build());

        mockMvc.perform(get("/loans/L1/emi-overview"))
                .andExpect(status().isOk());
    }
}
