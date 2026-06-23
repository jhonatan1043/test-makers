package com.bank.loans.infrastructure.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.bank.loans.domain.enums.LoanStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanResponse {
    private Long id;
    private BigDecimal amount;
    private Integer termMonths;
    private LoanStatus status;
    private LocalDateTime createdAt;
}