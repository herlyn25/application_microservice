package bootcamp.reto.powerup.model.states.gateways;

import bootcamp.reto.powerup.model.states.States;
import reactor.core.publisher.Mono;

public interface StatesRepository {
    Mono<States> findStateById(Long id);
}
