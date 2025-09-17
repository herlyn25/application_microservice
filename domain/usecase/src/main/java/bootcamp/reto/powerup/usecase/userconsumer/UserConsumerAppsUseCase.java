package bootcamp.reto.powerup.usecase.userconsumer;

import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerAppsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserConsumerAppsUseCase {
    private final UserConsumerAppsRepository userConsumerAppsRepository;

    public Mono<UserConsumer> userConsumerAppsRepository(String userEmail, String token) {
        return userConsumerAppsRepository.userGetToApps(userEmail, token);
    }
}
