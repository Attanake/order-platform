package arch.attanake.listener;

import arch.attanake.event.InventoryReservedEvent;
import arch.attanake.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    @KafkaListener(topics = "${kafka.topics.inventory-reserved}")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        orderService.processInventoryReservation(event);
    }
}
