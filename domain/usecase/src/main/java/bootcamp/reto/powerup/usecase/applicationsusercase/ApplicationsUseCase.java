package bootcamp.reto.powerup.usecase.applicationsusercase;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import bootcamp.reto.powerup.model.validations.ApplicationsDomainValidation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class ApplicationsUseCase {
    private final ApplicationsRepository  applicationsRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<Applications> saveApplication(Applications applications) {
        return ApplicationsDomainValidation.validateApplications(applications)
                .flatMap(appValidated-> loanTypeRepository.findLoanByCode(applications.getLoanType())
                        .flatMap(loanType->{
                            Applications saved = new Applications();
                            saved.setAmount(applications.getAmount());
                            saved.setTerms(applications.getTerms());
                            saved.setEmail(applications.getEmail());
                            saved.setStates(applications.getStates());
                            saved.setDocumentId(applications.getDocumentId());
                            saved.setLoanType(loanType.getName());
                            return Mono.just(saved);
                        }).flatMap(applicationsRepository::saveApps));
    }
}