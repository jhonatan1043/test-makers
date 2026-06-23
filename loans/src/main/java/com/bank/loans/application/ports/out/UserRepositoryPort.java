package com.bank.loans.application.ports.out;
import java.util.Optional;
import com.bank.loans.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(
            String email
    );
    Optional<User> findById(
            Long id
    );
}