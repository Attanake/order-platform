package arch.attanake.service;

import arch.attanake.dto.InventoryRequestDto;
import arch.attanake.dto.InventoryResponseDto;
import arch.attanake.dto.ReservationRequestDto;

public interface InventoryService {
    InventoryResponseDto checkStock(InventoryRequestDto request);
    void processReservation(ReservationRequestDto request);
    void cancelReservation(String orderId, String productId, int quantity);
}
