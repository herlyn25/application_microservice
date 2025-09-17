package bootcamp.reto.powerup.model.sqs.gateways;

import reactor.core.publisher.Mono;

public interface SQSRepository {

Mono<String> send(String message, String queueUrl);

}
