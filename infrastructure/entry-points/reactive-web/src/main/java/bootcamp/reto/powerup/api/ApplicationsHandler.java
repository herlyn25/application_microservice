package bootcamp.reto.powerup.api;

import bootcamp.reto.powerup.api.dto.ApplicationsDTO;
import bootcamp.reto.powerup.api.mapper.ApplicationMapper;
import bootcamp.reto.powerup.model.applications.Applications;
import bootcamp.reto.powerup.usecase.applicationsusercase.ApplicationsUseCase;
import bootcamp.reto.powerup.usecase.loantype.LoanTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ApplicationsHandler {
    private  final ApplicationsUseCase applicationsUseCase;
    private final ApplicationMapper applicationMapper;

    public Mono<ServerResponse> listenSaveApplications(ServerRequest serverRequest) {

        return  serverRequest.bodyToMono(ApplicationsDTO.class)
                .map(applicationMapper::dtoToApplications)
                .map(applicationsUseCase::saveApplication)
                .flatMap( applicationsSaved -> ServerResponse.created(URI.create("/api/v1/applications"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }
}