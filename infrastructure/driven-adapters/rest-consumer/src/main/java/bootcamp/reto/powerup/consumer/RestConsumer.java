package bootcamp.reto.powerup.consumer;

import bootcamp.reto.powerup.consumer.exceptions.JwtException;
import bootcamp.reto.powerup.model.ConstantsApps;
import bootcamp.reto.powerup.model.applications.gateways.ApplicationsRepository;
import bootcamp.reto.powerup.model.userconsumer.utils.PageResponse;
import bootcamp.reto.powerup.model.userconsumer.utils.UserConsumer;
import bootcamp.reto.powerup.model.userconsumer.UserConsumerFull;
import bootcamp.reto.powerup.model.userconsumer.gateways.UserConsumerRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserConsumerRepository {
    private final WebClient client;
    private final ApplicationsRepository  applicationsRepository;

    @Override
    public Mono<PageResponse<UserConsumerFull>> userGetApps(int page, int size, String token) {
        return isValidToken(token)
                .flatMap(tokenValid -> {
                    if (!tokenValid) {
                        return Mono.error(new JwtException(HttpStatus.UNAUTHORIZED,ConstantsApps.STATUS_401));
                    }

        Mono<List<UserConsumerFull>> userConsumersMono = applicationsRepository.findAllApps(page, size)
                .flatMap( apps -> userGet(apps.getEmail(),token)
                        .map(user -> new UserConsumerFull(apps, user))
        ).collectList();

        Mono<Long> totalElementsMono = applicationsRepository.findAllApps().count();
        Mono< BigDecimal > totalAmountApprobationMono = applicationsRepository.getTotalAmountApprobation();

        return Mono.zip(userConsumersMono,totalElementsMono, totalAmountApprobationMono)
                .map(tupleElements -> {
                    List<UserConsumerFull> userConsumerFullList = tupleElements.getT1();
                    Long totalElements = tupleElements.getT2();
                    BigDecimal totalApprobation = tupleElements.getT3();
                    int totalPages = (int) Math.ceil((double) totalElements/ size);
                    return new PageResponse<>(
                            userConsumerFullList,
                            page,
                            size,
                            totalElements,
                            totalPages,
                            totalApprobation);
                });
    });
    }
    @Override
    public Mono<UserConsumer> userGet(String email, String token) {
        return client.get()
                                .uri("/api/v1/users/{email}", email)
                                .header("Authorization", "Bearer " + token)
                                .retrieve()
                                .bodyToMono(UserConsumer.class);
    }
    @Override
    public Mono<Boolean> isValidToken(String token) {
        return Mono.fromCallable(() -> {
            if (token == null || token.isEmpty()) {
                throw new JwtException(HttpStatus.BAD_REQUEST,ConstantsApps.STATUS_400);
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