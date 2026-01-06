package com.lms.analytics.consumer;

import com.lms.analytics.dto.event.CustomerEvent;
import com.lms.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerAnalyticsConsumerTest {

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private CustomerAnalyticsConsumer consumer;

    @Test
    void shouldConsumeCustomerEvent() {

        CustomerEvent event = CustomerEvent.builder()
                .customerId("C1")
                .active(true)
                .build();

        consumer.consumeCustomerEvent(event);

        verify(analyticsService)
                .processCustomerEvent(event);
    }
}
