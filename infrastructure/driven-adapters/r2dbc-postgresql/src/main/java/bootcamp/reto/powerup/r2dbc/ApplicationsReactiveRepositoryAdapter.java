package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.r2dbc.entities.ApplicationsEntity;
import bootcamp.reto.powerup.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class ApplicationsReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Applications,
        ApplicationsEntity,
        Long,
        ApplicationsReactiveRepository
> implements ApplicationsRepository {
    private final TransactionalOperator transactionalOperator;
    public ApplicationsReactiveRepositoryAdapter(ApplicationsReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, Applications.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Applications> saveApps(Applications applications) {
        log.info("Iniciando el guardado de la solicitud de prestamo");
        return super.save(applications).as(transactionalOperator::transactional)
                .doOnNext(applicationsSaved-> log.trace("Solicitud de prestamo guardada correctamente."))
                .doOnError(error -> log.error("Error al guardar la solicitud de prestamo", error));
    }
}
