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
import software.amazon.awssdk.services.sqs.model.Message;

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
        String messageBody = message.body();
        log.info("Message body: {}", messageBody);
        JsonObject jsonObject = new JsonParser().parse(messageBody).getAsJsonObject();
        Long estado = jsonObject.get("estado").getAsLong();
        Long idCreditRequest = jsonObject.get("id_credit_request").getAsLong();
        log.info("Id Loan: {}, Id  Estado: {}", idCreditRequest, estado);
        return applicationsUseCase.updateApplication(idCreditRequest, estado).then();
    }
}
