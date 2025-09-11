package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.exceptions.ResourceNotFoundException;
import bootcamp.reto.powerup.model.states.States;
import bootcamp.reto.powerup.model.states.gateways.StatesRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.r2dbc.entities.ApplicationsEntity;
import bootcamp.reto.powerup.r2dbc.helper.ReactiveAdapterOperations;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Repository
public class ApplicationsReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Applications,
        ApplicationsEntity,
        Long,
        ApplicationsReactiveRepository
> implements ApplicationsRepository {
    private final TransactionalOperator transactionalOperator;
    private final StatesRepository statesRepository;

    public ApplicationsReactiveRepositoryAdapter(ApplicationsReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, StatesRepository statesRepository) {
        super(repository, mapper, entity -> mapper.map(entity, Applications.class));
        this.transactionalOperator = transactionalOperator;
        this.statesRepository = statesRepository;
    }

    @Override
    public Mono<Applications> saveApps(Applications applications) {
        log.info("Iniciando el guardado de la solicitud de prestamo para: {}", applications.getEmail());
        log.debug("Application data: {}", applications);

        return Mono.just(applications)
                .map(this::toData)
                .doOnNext(entity -> log.debug("Entity to save: {}", entity))
                .flatMap(entity -> repository.save(entity))
                .doOnNext(savedEntity -> log.debug("Entity saved: {}", savedEntity))
                .map(this::toEntity)
                .as(transactionalOperator::transactional)
                .doOnNext(applicationsSaved -> {
                    log.info("Solicitud de prestamo guardada correctamente con ID: {}", applicationsSaved.getId());
                    log.debug("Saved application: {}", applicationsSaved);
                })
                .doOnError(error -> log.error("Error al guardar la solicitud de prestamo", error));
    }

    @Override
    public Mono<Applications> updateApps(Long idApps, Long state) {
        return Mono.zip(validateIdApps(idApps), validateState(state))
                .flatMap(validatedIds -> {
                    Long validIdApps = validatedIds.getT1();
                    Long validState = validatedIds.getT2();

                    Mono<States> statesMono = statesRepository.findStateById(validState);
                    Mono<Applications> applicationsMono = findAppsById(validIdApps);

                    return Mono.zip(statesMono, applicationsMono);
                })
                .flatMap(tupleData -> {
                    States states = tupleData.getT1();
                    Applications applications = tupleData.getT2();
                    log.info("Estado de prestamo: {}", states.getName());
                    applications.setStates(states.getName());
                    return super.save(applications);
                });
    }

    @Override
    public Flux<ApplicationsResponse> findAllApps(int page, int size) {
        int offset =  (page - 1) * size;
        return super.repository.findAppsByPage(size, offset)
                .map(apps->mapper.map(apps,ApplicationsResponse.class));
    }

    @Override
    public Flux<ApplicationsResponse> findAllApps() {
        return super.repository.findAllAppsNew()
                .map(applications->mapper.map(applications,ApplicationsResponse.class));
    }
    @Override
    public Mono<BigDecimal> getTotalAmountApprobation() {
        return repository.totalAmountApprobation()
                .doOnNext(total -> log.debug("Total amount approbation: {}", total))
                .defaultIfEmpty(BigDecimal.ZERO); // En caso de que el resultado sea null
    }

    @Override
    public Mono<Applications> findAppsById(Long id) {
        return super.findById(id).switchIfEmpty(Mono.error(new ResourceNotFoundException(ConstantsApps.APPLICATIONS_NO_EXIST)));
    }

    private Mono<Long> validateState(Long id){
        if(id==null || id.intValue()<=0 ){
            return Mono.error(new ResourceNotFoundException(ConstantsApps.STATE_REQUERIED_FIELD));
        }
        return Mono.just(id);
    }

    private Mono<Long> validateIdApps(Long id){
        if(id==null || id.intValue()<=0 ){
            return Mono.error(new ResourceNotFoundException(ConstantsApps.ID_APPS_REQUERIED_FIELD));
        }
        return Mono.just(id);
    }
}