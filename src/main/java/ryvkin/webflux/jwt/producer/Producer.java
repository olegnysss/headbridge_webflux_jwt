package ryvkin.webflux.jwt.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ryvkin.webflux.jwt.service.KafkaMessagingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {

    private final KafkaMessagingService kafkaMessagingService;

    public void sendEvent(String username, String message) {
        kafkaMessagingService.sendEvent(username, message);
        log.info("Send message '{}' for user {}", message, username);
    }
}