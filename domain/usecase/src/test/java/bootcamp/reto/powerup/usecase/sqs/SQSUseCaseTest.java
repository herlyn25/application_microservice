package bootcamp.reto.powerup.usecase.sqs;

import bootcamp.reto.powerup.model.sqs.gateways.SQSRepository;
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
class SQSUseCaseTest {

    @Mock
    private SQSRepository sqsRepository;

    @InjectMocks
    private SQSUseCase sqsUseCase; // Reemplaza con el nombre real de tu clase

    private String testMessage;

    @BeforeEach
    void setUp() {
        testMessage = "Test message content";
    }

    @Test
    void sendMessage_ShouldReturnMessageId_WhenMessageSentSuccessfully() {
        // Given
        String expectedMessageId = "msg-12345";
        when(sqsRepository.send(testMessage))
                .thenReturn(Mono.just(expectedMessageId));

        // When
        Mono<String> result = sqsUseCase.sendMessage(testMessage);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedMessageId)
                .verifyComplete();

        verify(sqsRepository, times(1)).send(testMessage);
    }

    @Test
    void sendMessage_ShouldPropagateError_WhenRepositoryThrowsException() {
        // Given
        RuntimeException expectedException = new RuntimeException("SQS connection failed");
        when(sqsRepository.send(testMessage))
                .thenReturn(Mono.error(expectedException));

        // When
        Mono<String> result = sqsUseCase.sendMessage(testMessage);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(sqsRepository, times(1)).send(testMessage);
    }

    @Test
    void sendMessage_ShouldReturnEmpty_WhenRepositoryReturnsEmpty() {
        // Given
        when(sqsRepository.send(testMessage))
                .thenReturn(Mono.empty());

        // When
        Mono<String> result = sqsUseCase.sendMessage(testMessage);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(sqsRepository, times(1)).send(testMessage);
    }

    @Test
    void sendMessage_ShouldCallRepositoryWithCorrectParameter() {
        // Given
        String specificMessage = "Specific test message";
        when(sqsRepository.send(specificMessage))
                .thenReturn(Mono.just("msg-67890"));

        // When
        sqsUseCase.sendMessage(specificMessage).block();

        // Then
        verify(sqsRepository, times(1)).send(specificMessage);
    }

    @Test
    void sendMessage_ShouldHandleNullMessage() {
        // Given
        when(sqsRepository.send(null))
                .thenReturn(Mono.error(new IllegalArgumentException("Message cannot be null")));

        // When
        Mono<String> result = sqsUseCase.sendMessage(null);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(sqsRepository, times(1)).send(null);
    }

    @Test
    void sendMessage_ShouldHandleEmptyMessage() {
        // Given
        String emptyMessage = "";
        when(sqsRepository.send(emptyMessage))
                .thenReturn(Mono.just("msg-empty"));

        // When
        Mono<String> result = sqsUseCase.sendMessage(emptyMessage);

        // Then
        StepVerifier.create(result)
                .expectNext("msg-empty")
                .verifyComplete();

        verify(sqsRepository, times(1)).send(emptyMessage);
    }
}
