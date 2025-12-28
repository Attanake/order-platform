package arch.attanake.service;

import arch.attanake.dto.InventoryRequestDto;
import arch.attanake.dto.ReservationRequestDto;
import arch.attanake.exception.ProductNotFoundException;
import arch.attanake.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Component;


@GRpcService
@Component
@RequiredArgsConstructor
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryService inventoryService;

    @Override
    public void checkAvailability(AvailabilityRequest request,
                                  StreamObserver<AvailabilityResponse> responseObserver) {
        try {
            InventoryRequestDto requestDto = InventoryRequestDto.builder()
                    .productId(request.getProductId())
                    .build();

            var response = inventoryService.checkStock(requestDto);

            responseObserver.onNext(
                    AvailabilityResponse.newBuilder()
                            .setAvailable(response.getAvailable() >= request.getQuantity())
                            .setActualQuantity(response.getAvailable())
                            .build()
            );
            responseObserver.onCompleted();
        } catch (ProductNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Check availability error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void reserveItems(ReservationRequest request,
                             StreamObserver<ReservationResponse> responseObserver) {
        try {
            ReservationRequestDto requestDto = ReservationRequestDto.fromMap(
                    request.getOrderId(),
                    request.getItemsMap()
            );

            inventoryService.processReservation(requestDto);

            responseObserver.onNext(
                    ReservationResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Reservation completed")
                            .build()
            );
            responseObserver.onCompleted();
        } catch (ProductNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Reservation error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void releaseReservations(ReleaseRequest request,
                                    StreamObserver<ReleaseResponse> responseObserver) {
        try {
            inventoryService.cancelReservation(request.getOrderId(), "all", 0);

            responseObserver.onNext(
                    ReleaseResponse.newBuilder()
                            .setSuccess(true)
                            .build()
            );
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Release error: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}