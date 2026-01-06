package com.lms.payment.service;

import com.lms.payment.dto.PaymentRequest;
import com.lms.payment.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse makeEmiPayment(PaymentRequest request);

    List<PaymentResponse> getPaymentsByLoan(String loanId);

    List<PaymentResponse> getPaymentsByCustomer(String customerId);
}
