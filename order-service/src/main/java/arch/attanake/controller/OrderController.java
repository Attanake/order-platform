package arch.attanake.controller;

import arch.attanake.dto.CreateOrderRequestDto;
import arch.attanake.dto.OrderDto;
import arch.attanake.exception.InventoryReleaseException;
import arch.attanake.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody @Valid CreateOrderRequestDto request
    ) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDto>> getMyOrders(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable UUID orderId
    ) throws InventoryReleaseException {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
