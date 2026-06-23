package com.bank.loans.infrastructure.mapper;
import com.bank.loans.domain.model.User;
import com.bank.loans.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static User toDomain(
            UserEntity entity
    ){

        if(entity == null){
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

    public static UserEntity toEntity(
            User user
    ){
        if(user == null){
            return null;
        }
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

    }
}