package arch.attanake.event;

import arch.attanake.dto.UserCreatedEvent;
import arch.attanake.dto.UserRolesUpdatedEvent;
import arch.attanake.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final UserService userService;

    @KafkaListener(topics = "${spring.kafka.topics.user-created}")
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for user: {}", event.email());
        try {
            userService.processUserCreatedEvent(event);
        } catch (Exception e) {
            log.error("Error processing UserCreatedEvent: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.user-roles-updated}")
    public void handleUserRolesUpdatedEvent(UserRolesUpdatedEvent event) {
        log.info("Received UserRolesUpdatedEvent for user: {}", event.userId());
        try {
            userService.processUserRolesUpdatedEvent(event);
        } catch (Exception e) {
            log.error("Error processing UserRolesUpdatedEvent: {}", e.getMessage());
        }
    }
}
