package arch.attanake.streaming;


import arch.attanake.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import arch.attanake.avro.OrderCreated;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "order.created",
            groupId = "inventory-service-group",
            containerFactory = "kafkaAvroListenerContainerFactory"
    )
    public void handleOrderCreated(OrderCreated event){
        log.info("Received OrderCreated event: {}", event.getOrderId());
    }
}
