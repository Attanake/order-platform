package arch.attanake.streaming;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import arch.attanake.avro.InventoryReserved;

@Component
public class InventoryEventProducer {

    private final KafkaTemplate<String, InventoryReserved> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, InventoryReserved> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReservationSuccessEvent(String orderId) {
        InventoryReserved event = InventoryReserved.newBuilder()
                .setOrderId(orderId)
                .setSuccess(true)
                .build();

        kafkaTemplate.send("inventory.reserved", orderId, event);
    }

    public void sendReservationFailedEvent(String orderId, String reason) {
        InventoryReserved event = InventoryReserved.newBuilder()
                .setOrderId(orderId)
                .setSuccess(false)
                .setErrorMessage(reason)
                .build();

        kafkaTemplate.send("inventory.reserved", orderId, event);
    }
}
