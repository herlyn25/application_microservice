package bootcamp.reto.powerup.usecase.loantype;

import bootcamp.reto.powerup.model.exceptions.TypeLoanException;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeUseCase {
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanType> findLoanTypeByCode(String code){
        return loanTypeRepository.findLoanByCode(code)
                .switchIfEmpty(Mono.error(new TypeLoanException(code))
                );
    }
}