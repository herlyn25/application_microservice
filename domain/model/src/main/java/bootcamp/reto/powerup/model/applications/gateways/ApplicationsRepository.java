package bootcamp.reto.powerup.model.applications.gateways;

import bootcamp.reto.powerup.model.applications.ApplicationReports;
import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ApplicationsRepository {
    Mono<Applications> saveApps(Applications applications, String token);

    Mono<String> updateApps(Long id, Long state);

    Flux<ApplicationsResponse> findAllApps(int page, int size);

    Flux<ApplicationsResponse> findAllApps();

    Mono<BigDecimal> getTotalAmountApprobation();

    Mono<Applications> findAppsById(Long id);

    void findAppsApprovedByDate();
}

