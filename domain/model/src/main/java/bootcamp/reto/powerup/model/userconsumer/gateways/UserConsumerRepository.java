package bootcamp.reto.powerup.model.userconsumer.gateways;

import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import reactor.core.publisher.Mono;

public interface UserConsumerRepository {
Mono<PageResponse<UserConsumerFull>> userGetApps(int page, int size, String token);




}
