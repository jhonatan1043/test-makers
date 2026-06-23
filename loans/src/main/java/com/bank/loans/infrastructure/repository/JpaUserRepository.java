package com.bank.loans.infrastructure.persistence.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.loans.infrastructure.persistence.entity.UserEntity;


public interface JpaUserRepository
extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByEmail(
        String email
    );

}