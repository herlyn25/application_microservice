package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.states.States;
import bootcamp.reto.powerup.r2dbc.entities.StatesEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface StatesReactiveRepository extends ReactiveCrudRepository<StatesEntity, Long>, ReactiveQueryByExampleExecutor<StatesEntity> {

    @Query("SELECT * from states where id = :id")
    Mono<States> findStateById(Long id);
}
