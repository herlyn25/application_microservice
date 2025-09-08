package bootcamp.reto.powerup.consumer;

import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserConsumerRepository {
    private final WebClient client;
    private final ApplicationsRepository  applicationsRepository;

    @Override
    public Flux<UserConsumerFull> userGetApps(int size, int page) {
        return applicationsRepository.findAllApps(size,page)
                .flatMap( apps -> userGet(apps.getEmail())
                        .map(user -> new UserConsumerFull(apps, user))
        );
    }

    private Mono<UserConsumer> userGet(String email) {
        return client.get()
                .uri("/api/v1/users/{email}", email)
                .retrieve()
                .bodyToMono(UserConsumer.class);
    }
}