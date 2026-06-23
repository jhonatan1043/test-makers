package com.bank.loans.infrastructure.dto.request;
import com.bank.loans.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserRequest {

    @NotBlank(
        message = "Nombre requerido"
    )
    private String name;

    @Email(
        message = "Email inválido"
    )
    @NotBlank(
        message = "Email requerido"
    )
    private String email;

    @NotBlank(
        message = "Password requerido"
    )
    private String password;
    private Role role;

}