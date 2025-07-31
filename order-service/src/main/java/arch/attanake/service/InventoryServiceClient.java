package arch.attanake.service;

import arch.attanake.exception.InventoryException;
import arch.attanake.exception.InventoryServiceException;
import arch.attanake.grpc.AvailabilityResponse;
import arch.attanake.grpc.InventoryServiceGrpc;
import arch.attanake.grpc.ReleaseRequest;
import arch.attanake.grpc.ReleaseResponse;
import arch.attanake.grpc.ReservationRequest;
import arch.attanake.grpc.ReservationResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryServiceClient {

    private final ManagedChannel inventoryChannel;

    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

    public boolean checkAvailability(String productId, int quantity) {
        try {
            AvailabilityResponse response = inventoryStub.checkAvailability(
                    arch.attanake.grpc.AvailabilityRequest.newBuilder()
                            .setProductId(productId)
                            .setQuantity(quantity)
                            .build());

            return response.getAvailable();
        } catch (StatusRuntimeException e) {
            throw new InventoryServiceException("Inventory check failed: " + e.getStatus());
        }
    }

    public void reserveItems(String orderId, Map<String, Integer> items) {
        try {
            ReservationResponse response = inventoryStub.reserveItems(
                    ReservationRequest.newBuilder()
                            .setOrderId(orderId)
                            .putAllItems(items)
                            .build());

            if (!response.getSuccess()) {
                throw new InventoryServiceException("Reservation failed: " + response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            throw new InventoryServiceException("Reservation failed: " + e.getStatus());
        }
    }

    public void releaseReservations(UUID orderId) {
        InventoryServiceGrpc.InventoryServiceBlockingStub stub =
                InventoryServiceGrpc.newBlockingStub(inventoryChannel);

        ReleaseRequest request = ReleaseRequest.newBuilder()
                .setOrderId(orderId.toString())
                .build();

        ReleaseResponse response = stub.releaseReservations(request);

        if (!response.getSuccess()) {
            throw new InventoryException("Failed to release inventory reservations");
        }
    }
}
