package com.lms.loanapplication.service;

import com.lms.loanapplication.dto.*;

import java.util.List;

public interface LoanApplicationService {

    LoanApplicationResponse apply(
            String customerId,
            LoanApplicationRequest request);

    List<LoanApplicationResponse> getMyApplications(String customerId);
}
