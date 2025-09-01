package bootcamp.reto.powerup.api.dto;

import bootcamp.reto.powerup.model.states.StatesEnum;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

public record ApplicationsDTO (
    @NotBlank(message="Amount is required")
    BigDecimal amount,

    @NotBlank (message="terms is required")
    Integer terms,

    @NotBlank(message="email is required")
    String email,

    String states,

    String loanType,

    @NotBlank(message = "document id is required")
    String documentId
    ) {

    public ApplicationsDTO {
        System.out.println("Al entrar "+states);
        states = (states == null || states.trim().isEmpty()) ? states = StatesEnum.PENDIENTE.getCode() : states;
        System.out.println("Al salir " +states);
    }
}
