package arch.attanake.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemEvent {
    Long productId;
    Integer quantity;
    BigDecimal pricePerUnit;
}
