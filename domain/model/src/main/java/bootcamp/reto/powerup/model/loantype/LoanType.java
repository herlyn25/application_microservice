package bootcamp.reto.powerup.model.loantype;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {
    private Long id;
    private String uniqueCode;
    private String name;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
    private BigDecimal interestRate;
    private Boolean automaticValidation;
}
