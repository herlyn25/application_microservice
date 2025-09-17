package bootcamp.reto.powerup.usecase.userconsumer;

import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerAppsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserConsumerAppsUseCaseTest {
    @Mock
    private UserConsumerAppsRepository userConsumerAppsRepository;

    @InjectMocks
    private UserConsumerAppsUseCase userConsumerAppsUseCase; // Reemplaza con el nombre de tu clase

    private String userEmail;
    private String token;
    private UserConsumer expectedUserConsumer;

    @BeforeEach
    void setUp() {
        userEmail = "test@example.com";
        token = "test-token-123";
        expectedUserConsumer = new UserConsumer(); // Configura según tu modelo

    }

    @Test
    void userConsumerAppsRepository_WhenValidParameters_ShouldReturnUserConsumer() {
        // Given
        when(userConsumerAppsRepository.userGetToApps(userEmail, token))
                .thenReturn(Mono.just(expectedUserConsumer));

        // When
        Mono<UserConsumer> result = userConsumerAppsUseCase.userConsumerAppsRepository(userEmail, token);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedUserConsumer)
                .verifyComplete();

        verify(userConsumerAppsRepository).userGetToApps(userEmail, token);
    }

    @Test
    void userConsumerAppsRepository_WhenRepositoryReturnsEmpty_ShouldReturnEmptyMono() {
        // Given
        when(userConsumerAppsRepository.userGetToApps(userEmail, token))
                .thenReturn(Mono.empty());

        // When
        Mono<UserConsumer> result = userConsumerAppsUseCase.userConsumerAppsRepository(userEmail, token);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(userConsumerAppsRepository).userGetToApps(userEmail, token);
    }

    @Test
    void userConsumerAppsRepository_WhenRepositoryThrowsError_ShouldPropagateError() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database error");
        when(userConsumerAppsRepository.userGetToApps(userEmail, token))
                .thenReturn(Mono.error(expectedException));

        // When
        Mono<UserConsumer> result = userConsumerAppsUseCase.userConsumerAppsRepository(userEmail, token);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(userConsumerAppsRepository).userGetToApps(userEmail, token);
    }

    @Test
    void userConsumerAppsRepository_WhenNullEmail_ShouldCallRepositoryWithNullEmail() {
        // Given
        String nullEmail = null;
        when(userConsumerAppsRepository.userGetToApps(nullEmail, token))
                .thenReturn(Mono.just(expectedUserConsumer));

        // When
        Mono<UserConsumer> result = userConsumerAppsUseCase.userConsumerAppsRepository(nullEmail, token);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedUserConsumer)
                .verifyComplete();

        verify(userConsumerAppsRepository).userGetToApps(nullEmail, token);
    }

    @Test
    void userConsumerAppsRepository_WhenNullToken_ShouldCallRepositoryWithNullToken() {
        // Given
        String nullToken = null;
        when(userConsumerAppsRepository.userGetToApps(userEmail, nullToken))
                .thenReturn(Mono.just(expectedUserConsumer));

        // When
        Mono<UserConsumer> result = userConsumerAppsUseCase.userConsumerAppsRepository(userEmail, nullToken);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedUserConsumer)
                .verifyComplete();

        verify(userConsumerAppsRepository).userGetToApps(userEmail, nullToken);
    }

    @Test
    void userConsumerAppsRepository_WhenEmptyEmail_ShouldCallRepositoryWithEmptyEmail() {
        // Given
        String emptyEmail = "";
        when(userConsumerAppsRepository.userGetToApps(emptyEmail, token))
                .thenReturn(Mono.just(expectedUserConsumer));

        // When
        Mono<UserConsumer> result = userConsumerAppsUseCase.userConsumerAppsRepository(emptyEmail, token);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedUserConsumer)
                .verifyComplete();

        verify(userConsumerAppsRepository).userGetToApps(emptyEmail, token);
    }

    @Test
    void userConsumerAppsRepository_ShouldPassExactParametersToRepository() {
        // Given
        when(userConsumerAppsRepository.userGetToApps(userEmail, token))
                .thenReturn(Mono.just(expectedUserConsumer));

        // When
        userConsumerAppsUseCase.userConsumerAppsRepository(userEmail, token);

        // Then
        verify(userConsumerAppsRepository).userGetToApps(eq(userEmail), eq(token));
        verifyNoMoreInteractions(userConsumerAppsRepository);
    }
}
