package bootcamp.reto.powerup.usecase.userconsumer;

import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class UserConsumerUseCase {
    private final  UserConsumerRepository repository;

    public Flux<UserConsumerFull> userConsumerGet(int size, int page) {
        return repository.userGetApps(size, page);
    }
}