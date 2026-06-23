package com.bank.loans.application.ports.out;
import java.util.List;
import java.util.Optional;
import com.bank.loans.domain.model.Loan;

public interface LoanRepositoryPort {

    Loan save(
            Loan loan
    );

    Optional<Loan> findById(
            Long id
    );

    List<Loan> findByUserId(
            Long userId
    );

    List<Loan> findAll();
}