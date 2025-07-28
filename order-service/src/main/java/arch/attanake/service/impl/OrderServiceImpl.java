package arch.attanake.service.impl;

import arch.attanake.dto.CreateOrderRequestDto;
import arch.attanake.dto.OrderDto;
import arch.attanake.entity.OrderEntity;
import arch.attanake.entity.OrderItemEntity;
import arch.attanake.entity.OrderStatus;
import arch.attanake.event.InventoryReservedEvent;
import arch.attanake.event.OrderCreatedEvent;
import arch.attanake.event.OrderStatusChangedEvent;
import arch.attanake.exception.InventoryReleaseException;
import arch.attanake.exception.OrderNotFoundException;
import arch.attanake.mapper.OrderMapper;
import arch.attanake.repository.OrderRepository;
import arch.attanake.service.InventoryServiceClient;
import arch.attanake.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderMapper orderMapper;
    private final InventoryServiceClient inventoryServiceClient;


    @Override
    @Transactional
    public void processInventoryReservation(InventoryReservedEvent event) {
        UUID orderId = event.getOrderId();

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() != OrderStatus.NEW) {
            log.warn("Order {} is not in NEW state, current status: {}", orderId, order.getStatus());
            return;
        }

        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        log.info("Order {} status updated to PROCESSING after inventory reservation", orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(UUID orderId) throws InventoryReleaseException {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getStatus().isCancellable()) {
            throw new IllegalStateException("Order cannot be canceled in current status: " + order.getStatus());
        }

        releaseInventoryReservations(order);

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        OrderStatusChangedEvent event = new OrderStatusChangedEvent(orderId, OrderStatus.CANCELLED);
        kafkaTemplate.send("order.status-changed", orderId.toString(), event);
    }

    private void releaseInventoryReservations(OrderEntity order) throws InventoryReleaseException {
        try {
            inventoryServiceClient.releaseReservations(order.getId());
        } catch (Exception e) {
            log.error("Failed to release inventory reservations for order {}", order.getId(), e);
            throw new InventoryReleaseException("Failed to release inventory reservations");
        }
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequestDto request) {
        UUID currentUserId = getCurrentUserId();

        OrderEntity order = new OrderEntity();
        order.setUserId(currentUserId);
        order.setStatus(OrderStatus.NEW);

        request.getItems().forEach(item -> {
            OrderItemEntity orderItem = orderMapper.toItemEntity(item);
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        });

        OrderEntity savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = orderMapper.toOrderCreatedEvent(savedOrder);
        kafkaTemplate.send("order.created", savedOrder.getId().toString(), event);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return UUID.fromString(jwt.getClaim("user_id"));
        }

        throw new SecurityException("Cannot extract user ID from authentication");
    }
}