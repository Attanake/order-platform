package arch.attanake.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class OrderCreatedEvent {
    UUID orderId;
    List<OrderItemEvent> items;
}
