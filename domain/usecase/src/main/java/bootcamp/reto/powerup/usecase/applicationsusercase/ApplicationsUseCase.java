package bootcamp.reto.powerup.usecase.applicationsusercase;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationsUseCase {
    private final ApplicationsRepository  applicationsRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<Applications> saveApplication(Applications applications) {
        return  loanTypeRepository.findLoanByCode(applications.getLoanType().getUniqueCode())
                .flatMap(loantype->applicationsRepository.saveApplication(applications))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "No exists Loan Type "+ applications.getLoanType().getUniqueCode()))
                );
    }
}