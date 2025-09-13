package bootcamp.reto.powerup.usecase.sqs;

import bootcamp.reto.powerup.model.sqs.gateways.SQSRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SQSUseCase {
    private final SQSRepository sqsRepository;
    public Mono<String> sendMessage(String message) {
        return sqsRepository.send(message);
    }
}
