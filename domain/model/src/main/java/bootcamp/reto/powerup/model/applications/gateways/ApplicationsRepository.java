package bootcamp.reto.powerup.model.applications.gateways;

import bootcamp.reto.powerup.model.applications.Applications;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationsRepository {
    Mono<Applications> saveApplication(Applications applications);
}
