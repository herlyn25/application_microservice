package bootcamp.reto.powerup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name="Applications", description="Operations about credit requests")
public class ApplicationsRouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/apps",
                    method = RequestMethod.POST,
                    consumes = { MediaType.APPLICATION_JSON_VALUE },
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = ApplicationsHandler.class,
                    beanMethod = "listenSaveApplications",
                    operation = @Operation(
                            operationId = "saveApplications",
                            summary = "Crear solicitudes de credito",
                            tags = { "Applications" },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de la solicitud de prestamo a crear",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = bootcamp.reto.powerup.api.dto.ApplicationsDTO.class // ← cambia al paquete real de tu DTO si es distinto
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Creado",
                                            content = @Content(
                                                    schema = @Schema(implementation = bootcamp.reto.powerup.model.applications.Applications.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/apps",
                    method = RequestMethod.GET,
                    consumes = { MediaType.APPLICATION_JSON_VALUE },
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = ApplicationsHandler.class,
                    beanMethod = "listenAppsConsumer",
                    operation = @Operation(
                            operationId = "AppConsummerAuth",
                            summary = "Consultar las solicitudes pendientes de forma paginada y el total de deuda de las aprobadas",
                            tags = { "Applications" },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de las solicitudes de prestamo consultadas",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = bootcamp.reto.powerup.model.userconsumer.utils.PageResponse.class // ← cambia al paquete real de tu DTO si es distinto
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "oBTENER LISTADObtener Listado",
                                            content = @Content(
                                                    schema = @Schema(implementation = bootcamp.reto.powerup.model.applications.Applications.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Error de formato de Token o no paso token"),
                                    @ApiResponse(responseCode = "401", description = "No Autorizado indica una falla en la autenticación"),
                                    @ApiResponse(responseCode = "403", description = "No autorizado para recurso aun con token")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(ApplicationsHandler applicationsHandler) {
        return route(POST("/api/v1/apps"), applicationsHandler::listenSaveApplications)
                .andRoute(GET("/api/v1/apps"), applicationsHandler::listenAppsConsumer);
    }
}
