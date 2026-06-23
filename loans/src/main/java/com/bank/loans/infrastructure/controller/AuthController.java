package com.bank.loans.infrastructure.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.security.Principal;
import com.bank.loans.application.service.UserService;
import com.bank.loans.infrastructure.dto.request.UserRequest;
import com.bank.loans.infrastructure.dto.request.LoginRequest;
import com.bank.loans.infrastructure.dto.response.UserResponse;
import com.bank.loans.infrastructure.dto.response.LoginResponse;
import com.bank.loans.infrastructure.mapper.UserDtoMapper;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(

            @Valid
            @RequestBody
            UserRequest request

    ){


        var user =
                service.register(

                    UserDtoMapper.toDomain(
                            request
                    )

                );


        return UserDtoMapper
                .toResponse(
                        user
                );

    }

    @PostMapping("/login")
    public LoginResponse login(

            @RequestBody
            LoginRequest request

    ){

        var user =
                service.login(
                    request.getEmail(),
                    request.getPassword()
                );

        return LoginResponse.builder()
                .token(user.getPassword())
                .build();

    }

    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        return UserDtoMapper.toResponse(
            service.findByEmail(principal.getName())
        );
    }

}