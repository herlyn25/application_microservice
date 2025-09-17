package bootcamp.reto.powerup.usecase.application;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import bootcamp.reto.powerup.model.validations.ApplicationsDomainValidation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class ApplicationsUseCase {
    private final ApplicationsRepository  applicationsRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<Applications> saveApplication(Applications applications, String token) {
        return ApplicationsDomainValidation.validateApplications(applications)
                .flatMap(appValidated-> loanTypeRepository.findLoanByCode(applications.getLoanType())
                        .flatMap(loanType->{
                            Applications saved = new Applications();
                            saved.setAmount(applications.getAmount());
                            saved.setTerms(applications.getTerms());
                            saved.setEmail(applications.getEmail());
                            saved.setStates(applications.getStates());
                            saved.setDocumentId(applications.getDocumentId());
                            saved.setLoanType(loanType.getUniqueCode());
                            return applicationsRepository.saveApps(saved, token);
                        }));
    }
    public Mono<String> updateApplication(Long id, Long state) {
        return applicationsRepository.updateApps(id, state);
    }

}