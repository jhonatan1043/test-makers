package com.bank.loans.infrastructure.mapper;
import com.bank.loans.domain.model.Loan;
import com.bank.loans.infrastructure.persistence.entity.LoanEntity;

public class LoanMapper {
    public static Loan toDomain(
            LoanEntity entity
    ){
        return Loan.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .termMonths(entity.getTermMonths())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .user(
                    UserMapper.toDomain(
                        entity.getUser()
                    )
                )
                .build();

    }

    public static LoanEntity toEntity(
            Loan loan
    ){
        return LoanEntity.builder()
                .id(loan.getId())
                .amount(loan.getAmount())
                .termMonths(loan.getTermMonths())
                .status(loan.getStatus())
                .user(
                    UserMapper.toEntity(
                        loan.getUser()
                    )
                )
                .build();
    }
}