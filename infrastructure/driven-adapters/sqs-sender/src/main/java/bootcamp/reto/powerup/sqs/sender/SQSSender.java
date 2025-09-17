package bootcamp.reto.powerup.sqs.sender;

import bootcamp.reto.powerup.model.sqs.gateways.SQSRepository;
import bootcamp.reto.powerup.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements SQSRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;



    @Override
    public Mono<String> send(String message, String queueUrl) {
        return Mono.fromCallable(() -> buildRequest(message,queueUrl))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    /*private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }*/

    private SendMessageRequest buildRequest(String message, String queueUrl){
        return  SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
    }
}
