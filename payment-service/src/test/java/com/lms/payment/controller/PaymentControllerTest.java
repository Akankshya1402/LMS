package com.lms.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.payment.dto.PaymentRequest;
import com.lms.payment.dto.PaymentResponse;
import com.lms.payment.exception.GlobalExceptionHandler;
import com.lms.payment.exception.PaymentAlreadyProcessedException;
import com.lms.payment.model.enums.PaymentStatus;
import com.lms.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class) // ⭐ THIS IS THE KEY LINE
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // 201 CREATED
    // =========================
    @Test
    void shouldReturn201OnSuccessfulPayment() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setLoanId("LN1");
        request.setCustomerId("CUST1");
        request.setEmiNumber(1);
        request.setAmount(BigDecimal.valueOf(5000));

        PaymentResponse response = PaymentResponse.builder()
                .paymentId("PAY1")
                .loanId("LN1")
                .emiNumber(1)
                .amount(BigDecimal.valueOf(5000))
                .status(PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(paymentService.makeEmiPayment(any(PaymentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    // =========================
    // 400 BAD REQUEST
    // =========================
    @Test
    void shouldReturn400WhenAmountMissing() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setLoanId("LN1");

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenRequestInvalid() throws Exception {

        PaymentRequest request = new PaymentRequest();

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // =========================
    // 409 CONFLICT
    // =========================
    @Test
    void shouldReturn409WhenEmiAlreadyPaid() throws Exception {

        when(paymentService.makeEmiPayment(any()))
                .thenThrow(new PaymentAlreadyProcessedException("Already paid"));

        PaymentRequest request = new PaymentRequest();
        request.setLoanId("L1");
        request.setCustomerId("C1");
        request.setEmiNumber(1);
        request.setAmount(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // =========================
    // 500 INTERNAL SERVER ERROR
    // =========================
    @Test
    void shouldReturn500ForUnhandledException() throws Exception {

        when(paymentService.makeEmiPayment(any()))
                .thenThrow(new RuntimeException("Unexpected"));

        PaymentRequest request = new PaymentRequest();
        request.setLoanId("L1");
        request.setCustomerId("C1");
        request.setEmiNumber(1);
        request.setAmount(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unexpected"));
    }
    @Test
    void shouldReturn400ForMalformedJson() throws Exception {

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturn400ForEmptyBody() throws Exception {

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
