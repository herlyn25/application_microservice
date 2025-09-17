package bootcamp.reto.powerup.model.userconsumer.gateways;

import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import reactor.core.publisher.Mono;

public interface UserConsumerAppsRepository {
    public Mono<UserConsumer> userGetToApps(String email, String token);
    public Mono<Boolean> isValidToken(String token);
}
