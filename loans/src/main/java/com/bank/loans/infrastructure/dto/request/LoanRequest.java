package com.bank.loans.infrastructure.dto.request;
import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {

    @NotNull(
        message = "El monto es obligatorio"
    )
    @Min(
        value = 1,
        message = "El monto debe ser mayor a cero"
    )
    private BigDecimal amount;

    @NotNull(
        message = "El plazo es obligatorio"
    )
    @Min(
        value = 1,
        message = "El plazo debe ser mayor a cero"
    )
    private Integer termMonths;
}