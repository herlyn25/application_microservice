package bootcamp.reto.powerup.consumer;

import bootcamp.reto.powerup.consumer.exceptions.JwtException;
import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.exceptions.ResourceNotFoundException;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerAppsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestConsumerApps implements UserConsumerAppsRepository {
    private final WebClient client;

    @Override
    public Mono<UserConsumer> userGetToApps(String email, String token) {
       return client.get()
                .uri("/api/v1/users/{email}", email)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.error(new ResourceNotFoundException(ConstantsApps.USER_NO_EXIST)))
                .bodyToMono(UserConsumer.class);
    }

    @Override
    public Mono<Boolean> isValidToken(String token) {
        return Mono.fromCallable(() -> {
            if (token == null || token.isEmpty()) {
                throw new JwtException(HttpStatus.BAD_REQUEST,ConstantsApps.TOKEN_REQUIRED);
            }
            try {
                // Decodificar el token
                DecodedJWT decodedJWT = JWT.decode(token);
                Date expiresAt = decodedJWT.getExpiresAt();

                // Verificar si está expirado
                boolean isExpired = expiresAt.before(new Date());
                log.info("¿Está expirado? {}", isExpired ? "❌ SÍ" : "✅ NO");

                if (isExpired) {
                    log.error("Token expired");
                    throw new JwtException(HttpStatus.UNAUTHORIZED,ConstantsApps.TOKEN_EXPIRED);
                }
                return true;
            } catch (JWTDecodeException e) {
                log.error("Invalid token format: {}", e.getMessage());
                throw new JwtException(HttpStatus.UNAUTHORIZED,ConstantsApps.TOKEN_INVALID);
            }
        });
    }
}
