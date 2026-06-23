package com.bank.loans.application.service;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bank.loans.application.ports.out.LoanRepositoryPort;
import com.bank.loans.application.ports.out.UserRepositoryPort;
import com.bank.loans.domain.enums.LoanStatus;
import com.bank.loans.domain.model.Loan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepositoryPort loanRepository;
    private final UserRepositoryPort userRepository;

    @Transactional
    public Loan requestLoan(
            Loan loan,
            Long userId
    ){

        var user =
            userRepository
                .findById(userId)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Usuario no existe"
                    )
                );

        loan.setUser(user);
        loan.setStatus(
            LoanStatus.PENDING
        );

        return loanRepository
                .save(loan);

    }

    @Cacheable(
        value="loans",
        key="#userId"
    )
    public List<Loan> getUserLoans(
            Long userId
    ){


        return loanRepository
                .findByUserId(
                    userId
                );

    }

    @Transactional
    @CacheEvict(
        value="loans",
        allEntries=true
    )
    public Loan approveLoan(
            Long loanId
    ){


        var loan =
            loanRepository
                .findById(
                    loanId
                )
                .orElseThrow(
                    () ->
                    new RuntimeException(
                        "Préstamo no encontrado"
                    )
                );



        loan.setStatus(
            LoanStatus.APPROVED
        );


        return loanRepository
                .save(
                    loan
                );

    }

    @Transactional
    @CacheEvict(
        value="loans",
        allEntries=true
    )
    public Loan rejectLoan(
            Long loanId
    ){


        var loan =
            loanRepository
                .findById(
                    loanId
                )
                .orElseThrow(
                    () ->
                    new RuntimeException(
                        "Préstamo no encontrado"
                    )
                );



        loan.setStatus(
            LoanStatus.REJECTED
        );


        return loanRepository
                .save(
                    loan
                );

    }

    public List<Loan> getAll(){

        return loanRepository.findAll();

    }

}