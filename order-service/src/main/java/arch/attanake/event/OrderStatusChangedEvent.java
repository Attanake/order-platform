package arch.attanake.event;

import arch.attanake.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedEvent {
    private UUID orderId;
    private OrderStatus status;
}
