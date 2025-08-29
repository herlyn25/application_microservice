package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.r2dbc.entities.LoanTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, Long>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {

}
