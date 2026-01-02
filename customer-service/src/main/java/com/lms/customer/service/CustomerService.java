package com.lms.customer.service;

import com.lms.customer.dto.CustomerRequest;
import com.lms.customer.dto.CustomerResponse;
import com.lms.customer.exception.CustomerNotFoundException;
import com.lms.customer.model.Customer;
import com.lms.customer.model.enums.AccountStatus;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    // =========================
    // CREATE CUSTOMER
    // =========================
    public CustomerResponse create(CustomerRequest request) {

        Customer customer = Customer.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .monthlyIncome(request.getMonthlyIncome())
                .creditScore(650) // default/mock
                .existingEmiLiability(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .kycStatus(KycStatus.NOT_SUBMITTED)
                .emailVerified(false)
                .mobileVerified(false)
                .build();

        return mapToResponse(repository.save(customer));
    }

    // =========================
    // INTERNAL: VERIFIED CUSTOMER CHECK
    // =========================
    public Customer getVerifiedCustomerOrThrow(String customerId) {

        Customer customer = repository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: " + customerId)
                );

        if (customer.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Customer account is not active");
        }

        if (customer.getKycStatus() != KycStatus.VERIFIED) {
            throw new IllegalStateException("Customer KYC not verified");
        }

        if (!customer.isEmailVerified() || !customer.isMobileVerified()) {
            throw new IllegalStateException("Customer contact not verified");
        }

        return customer;
    }

    // =========================
    // INTERNAL: EMI LIABILITY UPDATE (FROM LOAN SERVICE)
    // =========================
    public void updateEmiLiability(String customerId, BigDecimal deltaAmount) {

        Customer customer = repository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customer.setExistingEmiLiability(
                customer.getExistingEmiLiability().add(deltaAmount)
        );

        repository.save(customer);
    }

    // =========================
    // ADMIN: KYC STATUS UPDATE
    // =========================
    public void updateKycStatus(String customerId, KycStatus status) {

        Customer customer = repository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customer.setKycStatus(status);
        repository.save(customer);
    }

    // =========================
    // ADMIN: VERIFY CONTACT DETAILS
    // =========================
    public void verifyEmail(String customerId) {
        Customer customer = repository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customer.setEmailVerified(true);
        repository.save(customer);
    }

    public void verifyMobile(String customerId) {
        Customer customer = repository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customer.setMobileVerified(true);
        repository.save(customer);
    }

    // =========================
    // GET ALL CUSTOMERS (ADMIN)
    // =========================
    public Page<CustomerResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // =========================
    // GET CUSTOMER BY ID (ADMIN)
    // =========================
    public CustomerResponse getById(String customerId) {

        Customer customer = repository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: " + customerId)
                );

        return mapToResponse(customer);
    }

    // =========================
    // ENTITY → DTO MAPPER
    // =========================
    private CustomerResponse mapToResponse(Customer customer) {

        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .mobile(customer.getMobile())
                .monthlyIncome(customer.getMonthlyIncome())
                .creditScore(customer.getCreditScore())
                .accountStatus(customer.getAccountStatus())
                .kycStatus(customer.getKycStatus())
                .build();
    }
}


