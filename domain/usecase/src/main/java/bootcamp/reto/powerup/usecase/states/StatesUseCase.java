package bootcamp.reto.powerup.usecase.states;

import bootcamp.reto.powerup.model.states.States;
import bootcamp.reto.powerup.model.states.gateways.StatesRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StatesUseCase {
    private final StatesRepository statesRepository;
    public Mono<States> findStateById(Long id){
        return statesRepository.findStateById(id);
    }
}
