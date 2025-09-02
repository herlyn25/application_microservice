package bootcamp.reto.powerup.api;

import bootcamp.reto.powerup.api.dto.ApplicationsDTO;
import bootcamp.reto.powerup.api.mapper.ApplicationMapper;
import bootcamp.reto.powerup.model.exceptions.ApplicationValidationException;
import bootcamp.reto.powerup.usecase.applicationsusercase.ApplicationsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApplicationsHandler {
    private  final ApplicationsUseCase applicationsUseCase;
    private final ApplicationMapper applicationMapper;

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
}