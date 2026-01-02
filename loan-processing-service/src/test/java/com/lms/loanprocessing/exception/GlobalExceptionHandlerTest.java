package com.lms.loanprocessing.exception;

import com.lms.loanprocessing.controller.LoanProcessingController;
import com.lms.loanprocessing.service.LoanProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanProcessingController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanProcessingService service;

    // =========================
    // LOAN NOT FOUND
    // =========================
    @Test
    void shouldReturn404WhenLoanNotFound() throws Exception {

        when(service.getLoan("X"))
                .thenThrow(new LoanNotFoundException("Loan not found"));

        mockMvc.perform(get("/loans/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Loan not found"));
    }

    // =========================
    // GENERIC ERROR
    // =========================
    @Test
    void shouldReturn500ForUnexpectedError() throws Exception {

        when(service.getLoan("Y"))
                .thenThrow(new RuntimeException("Boom"));

        mockMvc.perform(get("/loans/Y"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Internal server error"));
    }
}
