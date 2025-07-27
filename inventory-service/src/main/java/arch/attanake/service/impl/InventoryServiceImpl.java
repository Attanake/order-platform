package arch.attanake.service.impl;

import arch.attanake.dto.InventoryRequestDto;
import arch.attanake.dto.InventoryResponseDto;
import arch.attanake.dto.ReservationRequestDto;
import arch.attanake.entity.InventoryEntity;
import arch.attanake.exception.InsufficientStockException;
import arch.attanake.exception.ProductNotFoundException;
import arch.attanake.repository.InventoryRepository;
import arch.attanake.service.InventoryService;
import arch.attanake.streaming.InventoryEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryEventProducer eventProducer;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponseDto checkStock(InventoryRequestDto request) {
        InventoryEntity inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        return InventoryResponseDto.builder()
                .productId(inventory.getProductId())
                .total(inventory.getQuantity())
                .reserved(inventory.getReserved())
                .available(inventory.getQuantity() - inventory.getReserved())
                .build();
    }

    @Override
    @Transactional
    public void processReservation(ReservationRequestDto request) {
        request.getItems().forEach(item -> {
            InventoryEntity inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));

            if (inventory.getQuantity() - inventory.getReserved() < item.getQuantity()) {
                throw new InsufficientStockException(item.getProductId(),
                        inventory.getQuantity() - inventory.getReserved());
            }

            inventory.setReserved(inventory.getReserved() + item.getQuantity());
            inventoryRepository.save(inventory);
        });

        eventProducer.sendReservationSuccessEvent(request.getOrderId());
    }

    @Override
    @Transactional
    public void cancelReservation(String orderId, String productId, int quantity) {
        inventoryRepository.findByProductId(productId).ifPresent(inventory -> {
            inventory.setReserved(Math.max(0, inventory.getReserved() - quantity));
            inventoryRepository.save(inventory);
            log.info("Released {} items of product {} for order {}", quantity, productId, orderId);
        });
    }
}

