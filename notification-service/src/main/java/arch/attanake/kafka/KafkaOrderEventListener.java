package arch.attanake.kafka;

import arch.attanake.dto.CreateNotificationRequestDto;
import arch.attanake.entity.NotificationType;
import arch.attanake.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaOrderEventListener {

    /*NotificationService notificationService;

    @KafkaListener(topics = "order.status-changed")
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Received order status change: {}", event);

        CreateNotificationRequestDto request = new CreateNotificationRequestDto();
        request.setOrderId(event.getOrderId());
        request.setType(NotificationType.EMAIL);
        request.setMessage("Order status changed to " + event.getStatus());

        notificationService.createNotification(request);
    }*/
}
