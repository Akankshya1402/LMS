package com.lms.analytics.service;

import com.lms.analytics.dto.event.CustomerEvent;
import com.lms.analytics.dto.event.LoanEvent;
import com.lms.analytics.dto.event.PaymentEvent;
import com.lms.analytics.dto.response.DashboardResponse;

public interface AnalyticsService {

    void processLoanEvent(LoanEvent event);

    void processCustomerEvent(CustomerEvent event);

    void processPaymentEvent(PaymentEvent event);

    DashboardResponse getDashboard();
}
