package bootcamp.reto.powerup.usecase.userconsumer;

import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserConsumerUseCase {
    private final  UserConsumerRepository repository;

    public Mono<PageResponse<UserConsumerFull>> userConsumerGet(int size, int page, String token) {
        return repository.userGetApps(size, page, token);
    }
}