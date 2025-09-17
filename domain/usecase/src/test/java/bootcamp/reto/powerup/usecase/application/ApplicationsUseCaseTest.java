package bootcamp.reto.powerup.usecase.application;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private Applications inputApplication;
    private LoanType loanType;

    private Long testApplicationId;
    private Long testStateId;
    private String token = "mitokenfjkfkfkffkf";

    @BeforeEach
    void setUp() {
        testApplicationId = 1L;
        testStateId = 2L;

        inputApplication = new Applications();
        inputApplication.setAmount(new BigDecimal(10000.0));
        inputApplication.setTerms(12);
        inputApplication.setEmail("test@example.com");
        inputApplication.setStates("PENDING");
        inputApplication.setDocumentId("12345678");
        inputApplication.setLoanType("PERSONAL");

        loanType = new LoanType();
        loanType.setUniqueCode("PERSONAL");
        loanType.setName("Personal Loan");
    }

    @Test
    void saveApplication_WhenLoanTypeNotFound_ShouldCompleteEmpty() {
        // Given
        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication, token))
                .verifyComplete();
    }

    @Test
    void updateApplication_ShouldReturnSuccessMessage_WhenUpdateIsSuccessful() {
        // Given
        String expectedMessage = "Application updated successfully";
        when(applicationsRepository.updateApps(testApplicationId, testStateId))
                .thenReturn(Mono.just(expectedMessage));

        // When
        Mono<String> result = applicationsUseCase.updateApplication(testApplicationId, testStateId);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedMessage)
                .verifyComplete();

        verify(applicationsRepository, times(1)).updateApps(testApplicationId, testStateId);
    }

    @Test
    void updateApplication_ShouldPropagateError_WhenRepositoryThrowsException() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database update failed");
        when(applicationsRepository.updateApps(testApplicationId, testStateId))
                .thenReturn(Mono.error(expectedException));

        // When
        Mono<String> result = applicationsUseCase.updateApplication(testApplicationId, testStateId);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(applicationsRepository, times(1)).updateApps(testApplicationId, testStateId);
    }

    @Test
    void updateApplication_ShouldReturnEmpty_WhenApplicationNotFound() {
        // Given
        when(applicationsRepository.updateApps(testApplicationId, testStateId))
                .thenReturn(Mono.empty());

        // When
        Mono<String> result = applicationsUseCase.updateApplication(testApplicationId, testStateId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(applicationsRepository, times(1)).updateApps(testApplicationId, testStateId);
    }
}