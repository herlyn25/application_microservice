package bootcamp.reto.powerup.model.applications.gateways;

import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationsRepository {
    Mono<Applications> saveApps(Applications applications);
    Flux<ApplicationsResponse> findAllApps(int page, int size);
}
