package bootcamp.reto.powerup.api.dto;

import bootcamp.reto.powerup.model.states.StatesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public record ApplicationsDTO (
    @NotBlank(message="Amount is required")
    BigDecimal amount,

    @NotNull(message="terms is required")
    Integer terms,

    @NotBlank(message="email is required")
    String email,

    String states,

    String loanType,

    @NotBlank(message = "document id is required")
    String documentId,

    LocalDate created
    ) {

    public ApplicationsDTO {
        states = (states == null || states.trim().isEmpty()) ? states = StatesEnum.PENDIENTE.getName() : states;
        if (created == null) {
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            created = LocalDate.parse(formatter.format(now), formatter);
        }
    }
}
