package com.lms.customer.service;

import com.lms.customer.dto.CustomerRequest;
import com.lms.customer.dto.CustomerResponse;
import com.lms.customer.exception.CustomerNotFoundException;
import com.lms.customer.model.Customer;
import com.lms.customer.model.enums.AccountStatus;
import com.lms.customer.model.enums.KycStatus;
import com.lms.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService service;

    @Test
    void shouldCreateCustomer() {

        CustomerRequest request = CustomerRequest.builder()
                .fullName("John Doe")
                .email("john@test.com")
                .mobile("9876543210")
                .monthlyIncome(BigDecimal.valueOf(50000))
                .build();

        Customer savedCustomer = Customer.builder()
                .customerId("1")
                .fullName("John Doe")
                .email("john@test.com")
                .mobile("9876543210")
                .monthlyIncome(BigDecimal.valueOf(50000))
                .creditScore(650)
                .accountStatus(AccountStatus.ACTIVE)
                .kycStatus(KycStatus.NOT_SUBMITTED)
                .build();

        when(repository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponse response = service.create(request);

        assertEquals("John Doe", response.getFullName());
        assertEquals("john@test.com", response.getEmail());
        assertEquals(AccountStatus.ACTIVE, response.getAccountStatus());
        assertEquals(KycStatus.NOT_SUBMITTED, response.getKycStatus());

        verify(repository).save(any(Customer.class));
    }

    @Test
    void shouldReturnCustomerById() {

        Customer customer = Customer.builder()
                .customerId("1")
                .fullName("John Doe")
                .email("john@test.com")
                .mobile("9876543210")
                .monthlyIncome(BigDecimal.valueOf(40000))
                .creditScore(700)
                .accountStatus(AccountStatus.ACTIVE)
                .kycStatus(KycStatus.APPROVED)
                .build();

        when(repository.findById("1")).thenReturn(Optional.of(customer));

        CustomerResponse response = service.getById("1");

        assertEquals("John Doe", response.getFullName());
        assertEquals(KycStatus.APPROVED, response.getKycStatus());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        when(repository.findById("invalid"))
                .thenReturn(Optional.empty());

        CustomerNotFoundException exception =
                assertThrows(CustomerNotFoundException.class,
                        () -> service.getById("invalid"));

        assertEquals("Customer not found with id: invalid",
                exception.getMessage());
    }

}

