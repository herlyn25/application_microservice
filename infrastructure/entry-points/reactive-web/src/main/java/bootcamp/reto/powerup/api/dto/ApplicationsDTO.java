package bootcamp.reto.powerup.api.dto;

import bootcamp.reto.powerup.model.states.StatesEnum;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

public record ApplicationsDTO (
    @NotBlank(message="Amount is required")
    BigDecimal amount,

    @NotBlank(message="terms is required")
    Integer terms,

    @NotBlank(message="email is required")
    String email,

    StateDTO state,

    LoanTypeDTO loanType,

    @NotBlank(message = "document id is required")
    String documentId
    ) {

    public ApplicationsDTO {
        if (state.code()==null){
            StateDTO stateDTO = new StateDTO(
                    StatesEnum.PENDIENTE.getCode(),
                    StatesEnum.PENDIENTE.getName(),
                    StatesEnum.PENDIENTE.getDescription()
            );
            state = stateDTO;
        }
        if (!(loanType.uniqueCode() ==null)){
            LoanTypeDTO loanTypeDTO = new LoanTypeDTO(
                    loanType.uniqueCode(),
                    loanType.name(),
                    loanType.minimumAmount(),
                    loanType.maximumAmount(),
                    loanType.interestRate(),
                    loanType.automaticValidation()
            );
            loanType=loanTypeDTO;
        }
        loanType=null;
    }
}
