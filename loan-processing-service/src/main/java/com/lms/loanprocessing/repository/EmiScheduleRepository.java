package com.lms.loanprocessing.repository;

import com.lms.loanprocessing.model.EmiSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmiScheduleRepository
        extends MongoRepository<EmiSchedule, String> {

    List<EmiSchedule> findByLoanId(String loanId);
}
