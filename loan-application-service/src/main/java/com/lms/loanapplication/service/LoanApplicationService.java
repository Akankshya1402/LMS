package com.lms.loanapplication.service;

import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;

import java.util.List;

public interface LoanApplicationService {

    LoanApplicationResponse apply(
            String customerId,
            LoanApplicationRequest request);

    List<LoanApplicationResponse> getMyApplications(String customerId);

    List<LoanApplicationResponse> getPendingApplications();

    LoanApplicationResponse review(
            String applicationId,
            boolean approved,
            String remarks,
            String reviewer);
}
