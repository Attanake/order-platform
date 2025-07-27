package arch.attanake.service;

import arch.attanake.exception.InventoryException;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;
import arch.attanake.grpc.InventoryServiceGrpc;
import arch.attanake.grpc.ReleaseRequest;
import arch.attanake.grpc.ReleaseResponse;

@Component
@RequiredArgsConstructor
public class InventoryServiceClient {

    private final ManagedChannel inventoryChannel;

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
