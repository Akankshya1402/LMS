package com.lms.loanprocessing.model;

import com.lms.loanprocessing.model.enums.EmiStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document("emi_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmiSchedule {

    @Id
    private String emiId;

    private String loanId;
    private Integer emiNumber;
    private BigDecimal emiAmount;
    private LocalDate dueDate;
    private EmiStatus status;
}

