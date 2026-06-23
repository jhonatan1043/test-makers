package com.bank.loans.infrastructure.dto.response;
import com.bank.loans.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;

}