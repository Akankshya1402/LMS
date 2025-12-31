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
                .accountStatus(AccountStatus.ACTIVE)
                .kycStatus(KycStatus.NOT_SUBMITTED)
                .emailVerified(false)
                .mobileVerified(false)
                .build();

        Customer savedCustomer = repository.save(customer);
        return mapToResponse(savedCustomer);
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



