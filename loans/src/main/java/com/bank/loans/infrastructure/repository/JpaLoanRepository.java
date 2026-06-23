package com.bank.loans.infrastructure.persistence.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.loans.infrastructure.persistence.entity.LoanEntity;

public interface JpaLoanRepository
extends JpaRepository<LoanEntity, Long>{

    List<LoanEntity> findByUserId(
        Long userId
    );

}