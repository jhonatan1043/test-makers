package com.bank.loans.domain.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.bank.loans.domain.enums.LoanStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private Long id;
    private BigDecimal amount;
    private Integer termMonths;
    private LoanStatus status;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}