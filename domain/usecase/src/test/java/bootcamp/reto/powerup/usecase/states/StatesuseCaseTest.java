package bootcamp.reto.powerup.usecase.states;

import bootcamp.reto.powerup.model.states.States;
import bootcamp.reto.powerup.model.states.gateways.StatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FindStateByIdServiceTest {

    @Mock
    private StatesRepository statesRepository;

    @InjectMocks
    private StatesUseCase statesUseCase; // Reemplaza con el nombre real de tu clase

    private Long testStateId;
    private States testState;

    @BeforeEach
    void setUp() {
        testStateId = 1L;
        testState = States.builder()
                .name("Active")
                .description("Active state")
                .build();
        // O usa constructor si no tienes builder:
        // testState = new States(testStateId, "Active", "Active state");
    }

    @Test
    void findStateById_ShouldReturnState_WhenStateExists() {

        // Given
        when(statesRepository.findStateById(testStateId))
                .thenReturn(Mono.just(testState));

        // When
        Mono<States> result = statesUseCase.findStateById(testStateId);

        // Then
        StepVerifier.create(result)
                .expectNext(testState)
                .verifyComplete();

        verify(statesRepository, times(1)).findStateById(testStateId);
    }

    @Test
    void findStateById_ShouldReturnEmpty_WhenStateNotFound() {
        // Given
        when(statesRepository.findStateById(testStateId))
                .thenReturn(Mono.empty());

        // When
        Mono<States> result = statesUseCase.findStateById(testStateId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(statesRepository, times(1)).findStateById(testStateId);
    }

    @Test
    void findStateById_ShouldPropagateError_WhenRepositoryThrowsException() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database connection failed");
        when(statesRepository.findStateById(testStateId))
                .thenReturn(Mono.error(expectedException));

        // When
        Mono<States> result = statesUseCase.findStateById(testStateId);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(statesRepository, times(1)).findStateById(testStateId);
    }

    @Test
    void findStateById_ShouldCallRepositoryWithCorrectId() {
        // Given
        Long specificId = 999L;
        States specificState = States.builder()
                .name("Inactive")
                .description("Inactive state")
                .build();

        when(statesRepository.findStateById(specificId))
                .thenReturn(Mono.just(specificState));

        // When
        statesUseCase.findStateById(specificId).block();

        // Then
        verify(statesRepository, times(1)).findStateById(specificId);
    }

    @Test
    void findStateById_ShouldHandleNullId() {
        // Given
        when(statesRepository.findStateById(null))
                .thenReturn(Mono.error(new IllegalArgumentException("State ID cannot be null")));

        // When
        Mono<States> result = statesUseCase.findStateById(null);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(statesRepository, times(1)).findStateById(null);
    }

    @Test
    void findStateById_ShouldHandleZeroId() {
        // Given
        Long zeroId = 0L;
        when(statesRepository.findStateById(zeroId))
                .thenReturn(Mono.empty());

        // When
        Mono<States> result = statesUseCase.findStateById(zeroId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(statesRepository, times(1)).findStateById(zeroId);
    }

    @Test
    void findStateById_ShouldHandleNegativeId() {
        // Given
        Long negativeId = -1L;
        when(statesRepository.findStateById(negativeId))
                .thenReturn(Mono.error(new IllegalArgumentException("State ID must be positive")));

        // When
        Mono<States> result = statesUseCase.findStateById(negativeId);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(statesRepository, times(1)).findStateById(negativeId);
    }
}