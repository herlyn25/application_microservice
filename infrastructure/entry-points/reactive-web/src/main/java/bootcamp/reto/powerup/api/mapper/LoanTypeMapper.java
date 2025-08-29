package bootcamp.reto.powerup.api.mapper;

import bootcamp.reto.powerup.api.dto.LoanTypeDTO;
import bootcamp.reto.powerup.model.loantype.LoanType;
import org.springframework.stereotype.Component;

@Component
public class LoanTypeMapper {

    public LoanType dtoToLoanType(LoanTypeDTO loanTypeDTO){
        LoanType loanType = new LoanType(
                null,
                loanTypeDTO.uniqueCode(),
                loanTypeDTO.name(),
                loanTypeDTO.minimumAmount(),
                loanTypeDTO.maximumAmount(),
                loanTypeDTO.interestRate(),
                loanTypeDTO.automaticValidation()
        );
        return loanType;
    }
}
