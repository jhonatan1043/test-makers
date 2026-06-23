package com.bank.loans.infrastructure.persistence.adapter;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.bank.loans.application.ports.out.LoanRepositoryPort;
import com.bank.loans.domain.model.Loan;
import com.bank.loans.infrastructure.mapper.LoanMapper;
import com.bank.loans.infrastructure.persistence.repository.JpaLoanRepository;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class LoanRepositoryAdapter 
implements LoanRepositoryPort {
    private final JpaLoanRepository repository;

    @Override
    public Loan save(
            Loan loan
    ){

        var entity =
            LoanMapper.toEntity(
                loan
            );

        var saved =
            repository.save(
                entity
            );

        return LoanMapper
                .toDomain(
                    saved
                );

    }

    @Override
    public Optional<Loan> findById(
            Long id
    ){

        return repository
                .findById(id)
                .map(
                    LoanMapper::toDomain
                );

    }

    @Override
    public List<Loan> findByUserId(
            Long userId
    ){

        return repository
                .findByUserId(
                    userId
                )
                .stream()
                .map(
                    LoanMapper::toDomain
                )
                .toList();

    }

    @Override
    public List<Loan> findAll(){
        return repository
                .findAll()
                .stream()
                .map(
                    LoanMapper::toDomain
                )
                .toList();

    }
}