package com.bank.loans.infrastructure.mapper;
import com.bank.loans.domain.model.User;
import com.bank.loans.infrastructure.dto.request.UserRequest;
import com.bank.loans.infrastructure.dto.response.UserResponse;


public class UserDtoMapper {

    public static User toDomain(
            UserRequest request
    ){

        return User.builder()

                .name(
                    request.getName()
                )

                .email(
                    request.getEmail()
                )

                .password(
                    request.getPassword()
                )

                .role(
                    request.getRole()
                )

                .build();

    }

    public static UserResponse toResponse(
            User user
    ){


        return UserResponse.builder()

                .id(
                    user.getId()
                )

                .name(
                    user.getName()
                )

                .email(
                    user.getEmail()
                )

                .role(
                    user.getRole()
                )

                .build();

    }
}