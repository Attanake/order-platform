package arch.attanake.service;

import arch.attanake.dto.CreateOrderRequestDto;
import arch.attanake.dto.OrderDto;
import arch.attanake.exception.InventoryReleaseException;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto request);
    OrderDto getOrder(UUID id);
    List<OrderDto> getUserOrders(UUID userId);
    void cancelOrder(UUID id) throws InventoryReleaseException;
}
