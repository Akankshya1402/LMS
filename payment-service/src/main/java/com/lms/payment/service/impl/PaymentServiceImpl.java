package com.lms.payment.service.impl;

import com.lms.payment.client.CustomerClient;
import com.lms.payment.client.LoanProcessingClient;
import com.lms.payment.dto.PaymentRequest;
import com.lms.payment.dto.PaymentResponse;
import com.lms.payment.exception.PaymentAlreadyProcessedException;
import com.lms.payment.messaging.PaymentEventProducer;
import com.lms.payment.messaging.event.PaymentFailedEvent;
import com.lms.payment.messaging.event.PaymentSuccessEvent;
import com.lms.payment.model.Payment;
import com.lms.payment.model.enums.PaymentStatus;
import com.lms.payment.repository.PaymentRepository;
import com.lms.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final LoanProcessingClient loanClient;
    private final CustomerClient customerClient;
    private final PaymentEventProducer producer;

    @Override
    public PaymentResponse makeEmiPayment(PaymentRequest request) {

        if (repository.existsByLoanIdAndEmiNumberAndStatus(
                request.getLoanId(),
                request.getEmiNumber(),
                PaymentStatus.SUCCESS)) {

            throw new PaymentAlreadyProcessedException(
                    "EMI already paid for this loan");
        }

        Payment payment = Payment.builder()
                .loanId(request.getLoanId())
                .customerId(request.getCustomerId())
                .emiNumber(request.getEmiNumber())
                .amount(request.getAmount())
                .transactionRef(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .status(PaymentStatus.PENDING)
                .build();

        payment = repository.save(payment);

        try {
            // Simulate gateway success
            payment.setStatus(PaymentStatus.SUCCESS);
            repository.save(payment);

            loanClient.recordEmiPayment(
                    request.getLoanId(),
                    request.getEmiNumber()
            );

            customerClient.reduceEmiLiability(
                    request.getCustomerId(),
                    request.getAmount()
            );

            producer.publishPaymentSuccess(
                    PaymentSuccessEvent.builder()
                            .paymentId(payment.getPaymentId())
                            .customerId(payment.getCustomerId())
                            .loanId(payment.getLoanId())
                            .emiNumber(payment.getEmiNumber())
                            .amount(payment.getAmount())
                            .build()
            );

        } catch (Exception ex) {

            payment.setStatus(PaymentStatus.FAILED);
            repository.save(payment);

            producer.publishPaymentFailure(
                    PaymentFailedEvent.builder()
                            .customerId(request.getCustomerId())
                            .loanId(request.getLoanId())
                            .emiNumber(request.getEmiNumber())
                            .amount(request.getAmount())
                            .reason(ex.getMessage())
                            .build()
            );
        }

        return map(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByLoan(String loanId) {
        return repository.findByLoanId(loanId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByCustomer(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    private PaymentResponse map(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .loanId(payment.getLoanId())
                .emiNumber(payment.getEmiNumber())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionRef(payment.getTransactionRef())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
