package bootcamp.reto.powerup.usecase.userconsumer;

import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserConsumerUseCase Tests")
class UserConsumerUseCaseTest {

    @Mock
    private UserConsumerRepository repository;

    @InjectMocks
    private UserConsumerUseCase userConsumerUseCase;

    private PageResponse<UserConsumerFull> mockPageResponse;
    private List<UserConsumerFull> mockUserList;
    private String testEmail;
    private String testToken;
    private UserConsumer testUserConsumer;

    @BeforeEach
    void setUp() {
        ApplicationsResponse  applicationsResponse = ApplicationsResponse.builder()
                .amount(new BigDecimal("200"))
                .terms(12)
                .email("test@ht.com")
                .name("PR_STUDENT")
                .interest_rate(new BigDecimal("1.2"))
                .description("Prestamo para etudiantes").build();
        UserConsumer userConsumer = UserConsumer.builder()
                .fullname("Juan Perez")
                .salary(new BigDecimal("2349")).build();

        ApplicationsResponse  applicationsResponse2 = ApplicationsResponse.builder()
                .amount(new BigDecimal("2300"))
                .terms(24)
                .email("test@htr.com")
                .name("PR_VEHICULO")
                .interest_rate(new BigDecimal("1.8"))
                .description("Prestamo para compra vehiculos").build();
        UserConsumer userConsumer2 = UserConsumer.builder()
                .fullname("Juan Marquez")
                .salary(new BigDecimal("23349")).build();
        // Preparar datos mock
        UserConsumerFull user1 = new UserConsumerFull(applicationsResponse, userConsumer);
        UserConsumerFull user2 = new UserConsumerFull(applicationsResponse2, userConsumer2);


        mockUserList = Arrays.asList(user1, user2);

        mockPageResponse = PageResponse.<UserConsumerFull>builder()
                .content(mockUserList)
                .page(0)
                .size(10)
                .totalElements(2L)
                .totalPages(1)

                .build();
    }

    @Test
    @DisplayName("Debe retornar PageResponse vacío cuando no hay datos")
    void shouldReturnEmptyPageResponse() {
        // Given
        int size = 10;
        int page = 0;
        String token = "valid-token";

        PageResponse<UserConsumerFull> emptyResponse = PageResponse.<UserConsumerFull>builder()
                .content(Collections.emptyList())
                .page(0)
                .size(10)
                .totalElements(0L)
                .totalPages(0)
                .build();

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.just(emptyResponse));

        // When & Then
        StepVerifier.create(userConsumerUseCase.userConsumerGet(size, page, token))
                .expectNext(emptyResponse)
                .verifyComplete();

        verify(repository, times(1)).userGetApps(size, page, token);
    }

    @Test
    @DisplayName("Debe manejar error cuando el repository falla")
    void shouldHandleRepositoryError() {
        // Given
        int size = 10;
        int page = 0;
        String token = "invalid-token";
        RuntimeException expectedException = new RuntimeException("Repository error");

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.error(expectedException));

        // When & Then
        StepVerifier.create(userConsumerUseCase.userConsumerGet(size, page, token))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository, times(1)).userGetApps(size, page, token);
    }

    @Test
    @DisplayName("Debe funcionar con parámetros de paginación diferentes")
    void shouldWorkWithDifferentPaginationParameters() {
        // Given
        int size = 5;
        int page = 2;
        String token = "valid-token";

        PageResponse<UserConsumerFull> customPageResponse = PageResponse.<UserConsumerFull>builder()
                .content(mockUserList)
                .page(2)
                .size(5)
                .totalElements(15L)
                .totalPages(3)
                .build();

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.just(customPageResponse));

        // When & Then
        StepVerifier.create(userConsumerUseCase.userConsumerGet(size, page, token))
                .expectNext(customPageResponse)
                .verifyComplete();

        verify(repository, times(1)).userGetApps(size, page, token);
    }

    @Test
    @DisplayName("Debe pasar todos los parámetros correctamente al repository")
    void shouldPassAllParametersCorrectlyToRepository() {
        // Given
        int size = 20;
        int page = 1;
        String token = "specific-token-123";

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.just(mockPageResponse));

        // When
        userConsumerUseCase.userConsumerGet(size, page, token).block();

        // Then
        verify(repository, times(1)).userGetApps(eq(size), eq(page), eq(token));
        verify(repository, times(1)).userGetApps(20, 1, "specific-token-123");
    }

    @Test
    @DisplayName("Debe funcionar con token null")
    void shouldWorkWithNullToken() {
        // Given
        int size = 10;
        int page = 0;
        String token = null;

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.just(mockPageResponse));

        // When & Then
        StepVerifier.create(userConsumerUseCase.userConsumerGet(size, page, token))
                .expectNext(mockPageResponse)
                .verifyComplete();

        verify(repository, times(1)).userGetApps(size, page, token);
    }

    @Test
    @DisplayName("Debe funcionar con parámetros límite")
    void shouldWorkWithBoundaryParameters() {
        // Given
        int size = 0;
        int page = 0;
        String token = "";

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.just(mockPageResponse));

        // When & Then
        StepVerifier.create(userConsumerUseCase.userConsumerGet(size, page, token))
                .expectNext(mockPageResponse)
                .verifyComplete();

        verify(repository, times(1)).userGetApps(size, page, token);
    }

    @Test
    @DisplayName("Debe manejar Mono vacío del repository")
    void shouldHandleEmptyMonoFromRepository() {
        // Given
        int size = 10;
        int page = 0;
        String token = "valid-token";

        when(repository.userGetApps(size, page, token))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(userConsumerUseCase.userConsumerGet(size, page, token))
                .verifyComplete();

        verify(repository, times(1)).userGetApps(size, page, token);
    }

    @Test
    void userGet_ShouldPropagateError_WhenRepositoryThrowsException() {
        // Given
        RuntimeException expectedException = new RuntimeException("External service unavailable");
        when(repository.userGet(testEmail, testToken))
                .thenReturn(Mono.error(expectedException));

        // When
        Mono<UserConsumer> result = userConsumerUseCase.userGet(testEmail, testToken);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(repository, times(1)).userGet(testEmail, testToken);
    }

    @Test
    void userGet_ShouldReturnEmpty_WhenUserNotFound() {
        // Given
        when(repository.userGet(testEmail, testToken))
                .thenReturn(Mono.empty());

        // When
        Mono<UserConsumer> result = userConsumerUseCase.userGet(testEmail, testToken);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).userGet(testEmail, testToken);
    }
}