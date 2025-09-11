package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.exceptions.ResourceNotFoundException;
import bootcamp.reto.powerup.model.states.States;
import bootcamp.reto.powerup.model.states.gateways.StatesRepository;
import bootcamp.reto.powerup.r2dbc.entities.StatesEntity;
import bootcamp.reto.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class StatesReactiveRepositoryAdapter extends ReactiveAdapterOperations<States, StatesEntity, Long, StatesReactiveRepository>
implements StatesRepository {
    public StatesReactiveRepositoryAdapter(StatesReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity-> mapper.map(entity, States.class/* change for domain model */));
    }

    @Override
    public Mono<States> findStateById(Long id){
        return super.repository.findStateById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(ConstantsApps.NOT_FOUND_STATE)));
    }
}
