package bootcamp.reto.powerup.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record LoanTypeDTO(
        @NotBlank(message = "Code Loan is required")
        String uniqueCode,

        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Minimun Amount is required")
        BigDecimal minimumAmount,

        @NotBlank(message = "Maximun Amount is required")
        BigDecimal maximumAmount,

        @NotBlank(message = "Interest Rate is required")
        BigDecimal interestRate,

        @NotBlank(message = "Automatic Amount is required")
        Boolean automaticValidation
) {

}
