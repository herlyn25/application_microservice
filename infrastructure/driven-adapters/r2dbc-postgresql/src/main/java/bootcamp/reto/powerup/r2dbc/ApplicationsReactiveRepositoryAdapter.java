package bootcamp.reto.powerup.r2dbc;

import bootcamp.reto.powerup.model.loantype.LoanType;
import bootcamp.reto.powerup.model.loantype.gateways.LoanTypeRepository;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerAppsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsUserAppsResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import com.google.gson.Gson;
import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.exceptions.ResourceNotFoundException;
import bootcamp.reto.powerup.model.sqs.gateways.SQSRepository;
import bootcamp.reto.powerup.model.states.States;
import bootcamp.reto.powerup.model.states.gateways.StatesRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.ApplicationsResponse;
import bootcamp.reto.powerup.r2dbc.entities.ApplicationsEntity;
import bootcamp.reto.powerup.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

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
    private final SQSRepository sqsRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserConsumerAppsRepository userConsumerAppsRepository;

    @Value("${adapter.sqs.queueUrl}")
    private String queueUrl;

    @Value("${adapter.sqs.queue-capacity-url}")
    private String queueCapacityUrl;

    @Value("${adapter.sqs.queue-report}")
    private String queueReports;

    public ApplicationsReactiveRepositoryAdapter(ApplicationsReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, StatesRepository statesRepository, SQSRepository sqsRepository, LoanTypeRepository loanTypeRepository, UserConsumerAppsRepository userConsumerAppsRepository) {
        super(repository, mapper, entity -> mapper.map(entity, Applications.class));
        this.transactionalOperator = transactionalOperator;
        this.statesRepository = statesRepository;
        this.sqsRepository = sqsRepository;
        this.loanTypeRepository = loanTypeRepository;
        this.userConsumerAppsRepository = userConsumerAppsRepository;
    }

    @Override
    public Mono<Applications> saveApps(Applications applications, String token) {
        log.info("Iniciando el guardado de la solicitud de prestamo para: {}", applications.getEmail());
        log.debug("Application data: {}", applications);

        return Mono.just(applications)
                .map(this::toData)
                .doOnNext(savedEntity -> log.debug("Entity saved: {}", savedEntity))
                .flatMap(entity->repository.save(entity))
                .flatMap(applicationsEntity -> sendMessageSQS(mapper.map(applicationsEntity, Applications.class), token))
                .as(transactionalOperator::transactional)
                .doOnNext(applicationsSaved -> {
                    log.info("Solicitud de prestamo guardada correctamente con ID: {}", applicationsSaved.getId());
                    log.debug("Saved application: {}", applicationsSaved);
                })
                .doOnError(error -> log.error("Error al guardar la solicitud de prestamo", error));
    }

    @Override
    public Mono<String> updateApps(Long idApps, Long state) {
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
                    applications.setStates(states.getName());
                    log.info("Estado de prestamo cambiado a : {}", applications.getStates());
                    return super.save(applications);

                }).flatMap(appUpdated-> {
                    if(appUpdated.getStates().equals("APROB")) {
                        log.info("Body Actualizado a cola reports: {}", convertObjectToJSONString(appUpdated));
                        return sqsRepository.send(convertObjectToJSONString(appUpdated), queueReports);

                    }else if(appUpdated.getStates().equals("RCHZ")) {
                        log.info("Body a cola cambio estado: {}", convertObjectToJSONString(appUpdated));
                        return sqsRepository.send(convertObjectToJSONString(appUpdated), queueUrl);
                    }
                    log.info("No se envió mensaje a cola solicitudes");
                    return Mono.just(ConstantsApps.STATE_DIFFERENT_APROB_RECH);
                }).onErrorResume(error-> {
                    log.error("Error al actualizar aplicación - idApps: {}, state: {}, error: {}",
                            idApps, state, error.getMessage(), error);
                    return Mono.error(new ResourceNotFoundException(error.getMessage()));
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

    private Mono<Applications> sendMessageSQS(Applications applications, String token){
        Mono<List<ApplicationsResponse>> appsApproved = repository.findAppsByEmail(applications.getEmail())
                .map(entity -> mapper.map(entity, ApplicationsResponse.class))
                .collectList();
        Mono<UserConsumer> userConsumer = userConsumerAppsRepository.userGetToApps(applications.getEmail(), token);
        Mono<LoanType> loanTypeMono = loanTypeRepository.findLoanByCode(applications.getLoanType());
        Mono<Applications> applicationsMono = Mono.just(applications);

        log.info("Sending loan type ");
        return loanTypeMono.flatMap(loanType -> {
            if(loanType.getAutomaticValidation()){
                log.info(loanType.getUniqueCode());
                log.info("Enviar a cola");
                return Mono.zip(appsApproved,userConsumer, applicationsMono, loanTypeMono)
                        .flatMap(tupleElements -> {
                            List<ApplicationsResponse> listApps = tupleElements.getT1();
                            UserConsumer userConsumer1 = tupleElements.getT2();
                            Applications applications1 = tupleElements.getT3();
                            BigDecimal interest_rate = tupleElements.getT4().getInterestRate();
                            ApplicationsUserAppsResponse applicationsUserAppsResponse = ApplicationsUserAppsResponse.builder()
                                    .appsAproved(listApps)
                                    .userConsumer(userConsumer1)
                                    .applications(applications1)
                                    .interest_rate(interest_rate)
                                    .build();
                            return sqsRepository.send(convertObjectToJSONString(applicationsUserAppsResponse), queueCapacityUrl)
                                    .thenReturn(applications1);
                        });

            }
            log.info("No se envio a cola");
           return Mono.just(applications);
        });
    }

    private String convertObjectToJSONString(Object applications){
        Gson gson = new Gson();
        return gson.toJson(applications);
    }
}