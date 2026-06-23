package com.bank.loans.application.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bank.loans.application.ports.out.UserRepositoryPort;
import com.bank.loans.domain.enums.Role;
import com.bank.loans.domain.model.User;
import lombok.RequiredArgsConstructor;
import com.bank.loans.infrastructure.security.JwtService;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryPort repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public User register(
            User user
    ){

        user.setPassword(

            encoder.encode(
                user.getPassword()
            )

        );

        if(
            user.getRole() == null
        ){

            user.setRole(
                Role.USER
            );

        }
        return repository.save(
                user
        );
    }

    public User login(
        String email,
        String password
    ){

        var user =
            repository.findByEmail(email)

            .orElseThrow(
                () ->
                new RuntimeException(
                    "Usuario no existe"
                )
            );

        if(
            !encoder.matches(
                password,
                user.getPassword()
            )
        ){

            throw new RuntimeException(
                "Credenciales incorrectas"
            );

        }

        user.setPassword(
            jwtService.generateToken(user.getEmail())
        );

        return user;

    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}