package arch.attanake.mapper;

import arch.attanake.dto.CreateOrderRequestDto;
import arch.attanake.dto.OrderDto;
import arch.attanake.dto.OrderItemDto;
import arch.attanake.dto.OrderItemRequestDto;
import arch.attanake.entity.OrderEntity;
import arch.attanake.entity.OrderItemEntity;
import arch.attanake.event.OrderCreatedEvent;
import arch.attanake.event.OrderItemEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", source = "items")
    OrderEntity toEntity(CreateOrderRequestDto requestDto);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toItemEntity(OrderItemRequestDto requestDto);

    OrderItemDto toItemDto(OrderItemEntity entity);

    List<OrderItemDto> toItemDtoList(List<OrderItemEntity> entities);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "items", source = "items")
    OrderCreatedEvent toOrderCreatedEvent(OrderEntity entity);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "pricePerUnit", source = "pricePerUnit")
    OrderItemEvent toOrderItemEvent(OrderItemEntity entity);

    List<OrderItemEvent> toOrderItemEventList(List<OrderItemEntity> entities);
}
