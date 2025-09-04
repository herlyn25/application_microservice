package bootcamp.reto.powerup.usecase.application;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
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
    void saveApplication_WhenValidInput_ShouldSaveSuccessfully() {
        // Given
        Applications savedApplication = new Applications();
        savedApplication.setId(1L);
        savedApplication.setAmount(new BigDecimal(10000.0));
        savedApplication.setTerms(12);
        savedApplication.setEmail("test@example.com");
        savedApplication.setStates("PENDING");
        savedApplication.setDocumentId("12345678");
        savedApplication.setLoanType("Personal Loan");

        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.just(loanType));

        when(applicationsRepository.saveApps(any(Applications.class)))
                .thenReturn(Mono.just(savedApplication));

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication))
                .expectNext(savedApplication)
                .verifyComplete();

        verify(loanTypeRepository).findLoanByCode("PERSONAL");
        verify(applicationsRepository).saveApps(any(Applications.class));
    }

    @Test
    void saveApplication_WhenLoanTypeNotFound_ShouldCompleteEmpty() {
        // Given
        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication))
                .verifyComplete();

        verify(loanTypeRepository).findLoanByCode("PERSONAL");
        verify(applicationsRepository, never()).saveApps(any(Applications.class));
    }

    @Test
    void saveApplication_WhenLoanTypeRepositoryFails_ShouldReturnError() {
        // Given
        RuntimeException repositoryError = new RuntimeException("Database connection error");

        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.error(repositoryError));

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication))
                .expectError(RuntimeException.class)
                .verify();

        verify(loanTypeRepository).findLoanByCode("PERSONAL");
        verify(applicationsRepository, never()).saveApps(any(Applications.class));
    }

    @Test
    void saveApplication_WhenSaveRepositoryFails_ShouldReturnError() {
        // Given
        RuntimeException saveError = new RuntimeException("Save operation failed");

        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.just(loanType));

        when(applicationsRepository.saveApps(any(Applications.class)))
                .thenReturn(Mono.error(saveError));

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication))
                .expectError(RuntimeException.class)
                .verify();

        verify(loanTypeRepository).findLoanByCode("PERSONAL");
        verify(applicationsRepository).saveApps(any(Applications.class));
    }

    @Test
    void saveApplication_ShouldMapLoanTypeNameCorrectly() {
        // Given
        Applications savedApplication = new Applications();
        savedApplication.setLoanType("Personal Loan");

        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.just(loanType));

        when(applicationsRepository.saveApps(any(Applications.class)))
                .thenAnswer(invocation -> {
                    Applications app = invocation.getArgument(0);
                    // Verificar que el loanType se mapea correctamente
                    assert "Personal Loan".equals(app.getLoanType());
                    return Mono.just(savedApplication);
                });

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication))
                .expectNext(savedApplication)
                .verifyComplete();
    }

    @Test
    void saveApplication_ShouldPreserveAllApplicationFields() {
        // Given
        Applications savedApplication = new Applications();

        when(loanTypeRepository.findLoanByCode("PERSONAL"))
                .thenReturn(Mono.just(loanType));

        when(applicationsRepository.saveApps(any(Applications.class)))
                .thenAnswer(invocation -> {
                    Applications app = invocation.getArgument(0);
                    // Verificar que todos los campos se preservan
                    assert inputApplication.getAmount().equals(app.getAmount());
                    assert inputApplication.getTerms().equals(app.getTerms());
                    assert inputApplication.getEmail().equals(app.getEmail());
                    assert inputApplication.getStates().equals(app.getStates());
                    assert inputApplication.getDocumentId().equals(app.getDocumentId());
                    assert "Personal Loan".equals(app.getLoanType());
                    return Mono.just(savedApplication);
                });

        // When & Then
        StepVerifier.create(applicationsUseCase.saveApplication(inputApplication))
                .expectNext(savedApplication)
                .verifyComplete();
    }
}