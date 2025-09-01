package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.r2dbc.entities.ApplicationsEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import javax.swing.text.html.parser.Entity;

// TODO: This file is just an example, you should delete or modify it
public interface ApplicationsReactiveRepository extends ReactiveCrudRepository<ApplicationsEntity, Long>, ReactiveQueryByExampleExecutor<ApplicationsEntity> {

}
