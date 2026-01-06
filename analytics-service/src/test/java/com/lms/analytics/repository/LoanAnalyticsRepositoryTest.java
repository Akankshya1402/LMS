package com.lms.analytics.repository;

import com.lms.analytics.model.LoanAnalytics;
import com.lms.analytics.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@Import(TestMongoConfig.class)
class LoanAnalyticsRepositoryTest {

    @Autowired
    private LoanAnalyticsRepository repository;

    @Test
    void shouldSaveAndFetchAnalytics() {

        // given
        LoanAnalytics analytics = TestDataFactory.loanAnalytics();

        // when
        repository.save(analytics);

        // then
        assertThat(repository.findById(analytics.getLoanType()))
                .isPresent();
    }
}
