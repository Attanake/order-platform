package arch.attanake.controller;

import arch.attanake.dto.InventoryRequestDto;
import arch.attanake.dto.InventoryResponseDto;
import arch.attanake.dto.ReservationRequestDto;
import arch.attanake.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/check")
    public ResponseEntity<InventoryResponseDto> checkStock(
            @RequestBody InventoryRequestDto request
    ) {
        return ResponseEntity.ok(inventoryService.checkStock(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveItems(
            @RequestBody ReservationRequestDto request
    ) {
        inventoryService.processReservation(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/cancel/{orderId}/{productId}/{quantity}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable String orderId,
            @PathVariable String productId,
            @PathVariable int quantity
    ) {
        inventoryService.cancelReservation(orderId, productId, quantity);
        return ResponseEntity.ok().build();
    }
}
