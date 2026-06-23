package com.bank.loans.infrastructure.dto.response;

import com.bank.loans.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private Long userId;
    private String email;
    private Role role;
}