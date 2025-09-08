package bootcamp.reto.powerup.model.userconsumer.gateways;

import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import reactor.core.publisher.Flux;

public interface UserConsumerRepository {
Flux<UserConsumerFull> userGetApps(int page, int size);




}
