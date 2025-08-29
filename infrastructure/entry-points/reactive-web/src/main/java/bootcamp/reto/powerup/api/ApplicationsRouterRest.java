package bootcamp.reto.powerup.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ApplicationsRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(ApplicationsHandler applicationsHandler) {
        return route(POST("/api/v1/applications"), applicationsHandler::listenSaveApplications);
    }
}
