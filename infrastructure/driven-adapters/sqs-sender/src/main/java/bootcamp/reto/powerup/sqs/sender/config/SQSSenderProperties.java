package bootcamp.reto.powerup.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs")
public record SQSSenderProperties(
     String region,
     String queueUrl,
     String endpoint,
     Credentials credentials
){
    public record Credentials(
            String accessKey,
            String secretKey
    ) {}
}
