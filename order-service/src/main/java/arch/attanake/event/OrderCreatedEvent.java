package arch.attanake.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderCreatedEvent {
    Long orderId;
    List<OrderItemEvent> items;
}
