package bootcamp.reto.powerup.model.loantype.gateways;

import bootcamp.reto.powerup.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findLoanByCode(String code);
}
