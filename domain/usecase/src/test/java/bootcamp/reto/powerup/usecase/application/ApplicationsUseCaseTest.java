package bootcamp.reto.powerup.usecase.application;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class ApplicationsUseCaseTest {

    @Mock
    private ApplicationsRepository applicationsRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private ApplicationsUseCase applicationsUseCase;

    @Test
    void saveApplication_ok() {
        // Arrange
        Applications apps = new Applications();
        apps.setAmount(new BigDecimal(1000));
        apps.setTerms(12);
        apps.setEmail("test@test.com");
        apps.setStates("PENDING");
        apps.setDocumentId("1234567");
        apps.setLoanType("LOAN1");

        LoanType loanType = new LoanType();
        loanType.setUniqueCode("LOAN1");

        when(loanTypeRepository.findLoanByCode("LOAN1")).thenReturn(Mono.just(loanType));
        when(applicationsRepository.saveApps(any(Applications.class), any())).thenReturn(Mono.just(apps));

        // Act
        Mono<Applications> result = applicationsUseCase.saveApplication(apps, "token123");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(saved -> saved.getAmount().equals(new BigDecimal(1000))
                        && saved.getEmail().equals("test@test.com")
                        && saved.getLoanType().equals("LOAN1"))
                .verifyComplete();

        verify(loanTypeRepository, times(1)).findLoanByCode("LOAN1");
        verify(applicationsRepository, times(1)).saveApps(any(Applications.class), eq("token123"));
    }

    @Test
    void updateApplication_ok() {
        // Arrange
        when(applicationsRepository.updateApps(1L, 2L)).thenReturn(Mono.just("UPDATED"));

        // Act
        Mono<String> result = applicationsUseCase.updateApplication(1L, 2L);

        // Assert
        StepVerifier.create(result)
                .expectNext("UPDATED")
                .verifyComplete();

        verify(applicationsRepository, times(1)).updateApps(1L, 2L);
    }

    @Test
    void findAppsApprovByDate_callsRepository() {
        // Act
        applicationsUseCase.findAppsApprovByDate();

        // Assert
        verify(applicationsRepository, times(1)).findAppsApprovedByDate();
        verifyNoMoreInteractions(applicationsRepository);
    }
}