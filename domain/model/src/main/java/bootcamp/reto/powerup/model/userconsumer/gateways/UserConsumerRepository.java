package bootcamp.reto.powerup.model.userconsumer.gateways;

import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import reactor.core.publisher.Mono;

public interface UserConsumerRepository {
Mono<PageResponse<UserConsumerFull>> userGetApps(int page, int size, String token);
Mono<UserConsumer> userGet(String email, String token);
Mono<Boolean> isValidToken(String token);


}
