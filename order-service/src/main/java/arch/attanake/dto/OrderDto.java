package arch.attanake.dto;

import arch.attanake.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID id;
    private String orderNumber;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}
