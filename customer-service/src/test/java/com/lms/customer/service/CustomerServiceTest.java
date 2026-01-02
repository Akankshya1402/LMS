package com.lms.customer.service;

import com.lms.customer.dto.CustomerRequest;
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
    void shouldCreateCustomerWithDefaults() {

        CustomerRequest request = CustomerRequest.builder()
                .fullName("John Doe")
                .email("john@test.com")
                .mobile("9876543210")
                .monthlyIncome(BigDecimal.valueOf(50000))
                .build();

        when(repository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(request);

        assertEquals("John Doe", response.getFullName());
        assertEquals(KycStatus.NOT_SUBMITTED, response.getKycStatus());
    }

    @Test
    void shouldThrowIfCustomerNotFound() {

        when(repository.findById("X"))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> service.getById("X"));
    }

    @Test
    void shouldUpdateKycStatus() {

        Customer customer = Customer.builder()
                .customerId("C1")
                .kycStatus(KycStatus.PENDING)
                .accountStatus(AccountStatus.ACTIVE)
                .existingEmiLiability(BigDecimal.ZERO)
                .build();

        when(repository.findById("C1"))
                .thenReturn(Optional.of(customer));

        service.updateKycStatus("C1", KycStatus.VERIFIED);

        assertEquals(KycStatus.VERIFIED, customer.getKycStatus());
        verify(repository).save(customer);
    }
}

