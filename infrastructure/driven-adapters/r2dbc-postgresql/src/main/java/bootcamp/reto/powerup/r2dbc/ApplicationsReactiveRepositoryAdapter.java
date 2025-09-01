package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.r2dbc.entities.ApplicationsEntity;
import bootcamp.reto.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ApplicationsReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Applications,
        ApplicationsEntity,
        Long,
        ApplicationsReactiveRepository
> implements ApplicationsRepository {
    public ApplicationsReactiveRepositoryAdapter(ApplicationsReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Applications.class));
    }

    @Override
    public Mono<Applications> saveApps(Applications applications) {

        return super.save(applications);
    }
}
