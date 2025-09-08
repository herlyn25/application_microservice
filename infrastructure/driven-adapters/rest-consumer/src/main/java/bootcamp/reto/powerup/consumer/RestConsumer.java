package bootcamp.reto.powerup.consumer;

import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserConsumerRepository {
    private final WebClient client;
    private final ApplicationsRepository  applicationsRepository;

    @Override
    public Mono<PageResponse<UserConsumerFull>> userGetApps(int size, int page) {
        Mono<List<UserConsumerFull>> userConsumersMono = applicationsRepository.findAllApps(page, size)
                .flatMap( apps -> userGet(apps.getEmail())
                        .map(user -> new UserConsumerFull(apps, user))
        ).collectList();
        Mono<Long> totalElementsMono = applicationsRepository.findAllApps().count();
        Mono< BigDecimal > totalAmountApprobationMono = applicationsRepository.getTotalAmountApprobation();

        return Mono.zip(userConsumersMono,totalElementsMono, totalAmountApprobationMono)
                .map(tupeElements -> {
                    List<UserConsumerFull> userConsumerFullList = tupeElements.getT1();
                    Long totalElements = tupeElements.getT2();
                    BigDecimal totalApprobation = tupeElements.getT3();
                    int totalPages = (int) Math.ceil((double) totalElements/ size);
                    return new PageResponse<>(
                            userConsumerFullList,
                            page,
                            size,
                            totalElements,
                            totalPages,
                            totalApprobation);
                });
    }

    private Mono<UserConsumer> userGet(String email) {
        return client.get()
                .uri("/api/v1/users/{email}", email)
                .retrieve()
                .bodyToMono(UserConsumer.class);
    }
}