package com.bank.loans.infrastructure.persistence.adapter;


import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bank.loans.application.ports.out.UserRepositoryPort;
import com.bank.loans.domain.model.User;
import com.bank.loans.infrastructure.mapper.UserMapper;
import com.bank.loans.infrastructure.persistence.repository.JpaUserRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter 
implements UserRepositoryPort {

    private final JpaUserRepository repository;

    @Override
    public User save(
            User user
    ){

        var entity =
            UserMapper.toEntity(user);


        var saved =
            repository.save(entity);


        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(
            String email
    ){

        return repository
                .findByEmail(email)
                .map(
                    UserMapper::toDomain
                );

    }

    @Override
    public Optional<User> findById(
            Long id
    ){

        return repository
                .findById(id)
                .map(
                    UserMapper::toDomain
                );

    }

}