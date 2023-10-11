package ryvkin.webflux.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessagingService {

    @Value("${topic.auth-events}")
    private String authEventsTopic;

    private final KafkaTemplate<String , Object> kafkaTemplate;

    public void sendEvent(String username, String message) {
        kafkaTemplate.send(authEventsTopic, username, message);
    }
}