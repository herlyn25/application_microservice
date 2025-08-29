package bootcamp.reto.powerup.model.applications;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.states.States;
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
public class Applications {
    private Long id;
    private BigDecimal amount;
    private Integer terms;
    private String email;
    private States states;
    private LoanType loanType;
    private String documentId;

    public Applications(BigDecimal amount, Integer terms, String email, States states, LoanType loanType, String documentId) {
        this.amount = amount;
        this.terms = terms;
        this.email = email;
        this.states = states;
        this.loanType = loanType;
        this.documentId = documentId;
    }
}
