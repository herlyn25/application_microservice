package bootcamp.reto.powerup.sqs.listener;

import bootcamp.reto.powerup.usecase.application.ApplicationsUseCase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;
import software.amazon.awssdk.services.sqs.model.Message;

import java.time.Duration;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
     private final ApplicationsUseCase applicationsUseCase;
     private final Gson gson = new GsonBuilder()
             .setPrettyPrinting()
             .create();

    @Override
    public Mono<Void> apply(Message message) {
        return Mono.fromCallable(()-> {
            String messageBody = message.body();
            log.info("Message llega a listener: {}", messageBody);
            JsonObject jsonObject = new JsonParser().parse(messageBody).getAsJsonObject();
            Long estado = jsonObject.get("estado").getAsLong();
            Long idCreditRequest = jsonObject.get("id_credit_request").getAsLong();
            return Tuples.of(idCreditRequest, estado);
         }).flatMap(data-> applicationsUseCase.updateApplication(data.getT1(), data.getT2()))
                .timeout(Duration.ofSeconds(30))
                .doOnError(e-> log.error("Error procesando el mensaje {} : {}", message.messageId(), e.getMessage()))
                .then();
    }
}
