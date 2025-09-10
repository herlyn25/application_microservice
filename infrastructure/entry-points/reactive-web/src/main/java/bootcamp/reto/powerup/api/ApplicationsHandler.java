package bootcamp.reto.powerup.api;

import bootcamp.reto.powerup.api.dto.ApplicationsDTO;
import bootcamp.reto.powerup.api.mapper.ApplicationMapper;
import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.model.exceptions.ApplicationValidationException;
import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.usecase.application.ApplicationsUseCase;
import bootcamp.reto.powerup.usecase.userconsumer.UserConsumerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.*;
import java.net.URI;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ApplicationsHandler {
    private  final ApplicationsUseCase applicationsUseCase;
    private final ApplicationMapper applicationMapper;
    private final UserConsumerUseCase  userConsumerUseCase;


    public Mono<ServerResponse> listenSaveApplications(ServerRequest serverRequest) {
        return  serverRequest.bodyToMono(ApplicationsDTO.class)
                .map(applicationMapper::dtoToApplications)
                .flatMap(applicationsUseCase::saveApplication)
                .flatMap( applicationsSaved ->
                        ServerResponse.created(URI.create("/api/v1/apps"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                ).contextCapture().onErrorResume(ApplicationValidationException.class, ex ->
                    ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue( Map.of(
                                    "error","validation error",
                                    "message", ex.getMessage())
                    )
                );
    }
    public Mono<ServerResponse> listenFindAllApplications(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("1"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("5"));


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(applicationsUseCase.findAllAppsFlux(page, size),Applications.class);
    }

    public Mono<ServerResponse> listenAppsConsumer(ServerRequest serverRequest) {
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("5"));
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("1"));
        String token = serverRequest.queryParam("token").orElse(null);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userConsumerUseCase.userConsumerGet(page,size, token), UserConsumerFull.class);
    }
}