package com.lms.loanprocessing.exception;

import com.lms.loanprocessing.controller.LoanProcessingController;
import com.lms.loanprocessing.service.LoanServicingService;
import com.lms.loanprocessing.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanProcessingController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private LoanServicingService servicingService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturn404WhenLoanNotFound() throws Exception {

        when(loanRepository.findById("X"))
                .thenThrow(new LoanNotFoundException("Loan not found"));

        mockMvc.perform(get("/loans/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Loan not found"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturn500ForUnhandledException() throws Exception {

        when(loanRepository.findById("X"))
                .thenThrow(new RuntimeException("Boom"));

        mockMvc.perform(get("/loans/X"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Internal server error"));
    }
}

