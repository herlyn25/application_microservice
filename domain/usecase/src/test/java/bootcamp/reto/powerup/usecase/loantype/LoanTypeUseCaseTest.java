package bootcamp.reto.powerup.usecase.loantype;

import bootcamp.reto.powerup.model.exceptions.TypeLoanException;
import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanTypeUseCaseTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private LoanTypeUseCase loanTypeUseCase;

    @Test
    void testFindLoanTypeByCode_Found() {
        // Arrange
        String code = "VEHICLE";
        LoanType loanType = new LoanType();
        loanType.setUniqueCode(code);

        when(loanTypeRepository.findLoanByCode(code)).thenReturn(Mono.just(loanType));

        // Act & Assert
        StepVerifier.create(loanTypeUseCase.findLoanTypeByCode(code))
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void testFindLoanTypeByCode_NotFound() {
        // Arrange
        String code = "HOME";
        when(loanTypeRepository.findLoanByCode(code)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(loanTypeUseCase.findLoanTypeByCode(code))
                .expectError(TypeLoanException.class)
                .verify();
    }
}
