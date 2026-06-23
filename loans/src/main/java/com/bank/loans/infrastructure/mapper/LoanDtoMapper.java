package com.bank.loans.infrastructure.mapper;
import com.bank.loans.domain.model.Loan;
import com.bank.loans.infrastructure.dto.request.LoanRequest;
import com.bank.loans.infrastructure.dto.response.LoanResponse;


public class LoanDtoMapper {

    public static Loan toDomain(
            LoanRequest request
    ){


        return Loan.builder()

                .amount(
                    request.getAmount()
                )

                .termMonths(
                    request.getTermMonths()
                )

                .build();

    }

    public static LoanResponse toResponse(
            Loan loan
    ){

        return LoanResponse.builder()

                .id(
                    loan.getId()
                )

                .amount(
                    loan.getAmount()
                )

                .termMonths(
                    loan.getTermMonths()
                )

                .status(
                    loan.getStatus()
                )

                .createdAt(
                    loan.getCreatedAt()
                )
                .build();
    }

}