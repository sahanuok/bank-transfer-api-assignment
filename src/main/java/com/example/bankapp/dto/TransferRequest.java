package com.example.bankapp.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class TransferRequest {
    @NotBlank(message = "From account is required")
    private String fromAccount;

    @NotBlank(message = "To account is required")
    private String toAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum transfer amount is 1.00")
    private BigDecimal amount;

    private boolean simulateFailure;
}