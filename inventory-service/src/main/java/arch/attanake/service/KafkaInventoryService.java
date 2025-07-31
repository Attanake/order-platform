package arch.attanake.service;

import arch.attanake.dto.InventoryItemDto;
import arch.attanake.dto.ReservationRequestDto;
import arch.attanake.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import arch.attanake.avro.OrderCreated;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaInventoryService {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order.created", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreated event) {
        log.info("Processing order creation: {}", event.getOrderId());

        ReservationRequestDto request = new ReservationRequestDto();
        request.setOrderId(event.getOrderId().toString());

        event.getItems().forEach(item ->
                request.getItems().add(new InventoryItemDto(
                        item.getProductId().toString(),
                        item.getQuantity()
                ))
        );

        inventoryService.processReservation(request);
    }
}
