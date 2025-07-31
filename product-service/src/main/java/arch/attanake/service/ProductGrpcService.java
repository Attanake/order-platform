package arch.attanake.service;

import arch.attanake.dto.ProductResponseDto;
import arch.attanake.grpc.ProductListRequest;
import arch.attanake.grpc.ProductRequest;
import arch.attanake.grpc.ProductResponse;
import arch.attanake.grpc.ProductServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            ProductResponseDto dto = productService.getProductById(request.getId());
            responseObserver.onNext(mapToGrpcResponse(dto));
            responseObserver.onCompleted();
            log.info("Successfully processed GetProduct for ID: {}", request.getId());
        } catch (Exception e) {
            handleError(responseObserver, e, "GetProduct", request.getId());
        }
    }

    @Override
    public void getProducts(ProductListRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            request.getIdsList().forEach(id -> {
                try {
                    ProductResponseDto dto = productService.getProductById(id);
                    responseObserver.onNext(mapToGrpcResponse(dto));
                } catch (Exception e) {
                    log.warn("Failed to fetch product with ID: {}. Error: {}", id, e.getMessage());
                }
            });
            responseObserver.onCompleted();
            log.info("Processed GetProducts for {} IDs", request.getIdsCount());
        } catch (Exception e) {
            handleError(responseObserver, e, "GetProducts", null);
        }
    }

    private ProductResponse mapToGrpcResponse(ProductResponseDto dto) {
        return ProductResponse.newBuilder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setPrice(dto.getPrice().doubleValue())
                .setAvailable(dto.getStock() > 0)
                .build();
    }

    private void handleError(StreamObserver<?> responseObserver,
                             Exception e,
                             String methodName,
                             String productId) {
        String errorMsg = String.format("Error in %s%s: %s",
                methodName,
                productId != null ? " for product " + productId : "",
                e.getMessage());

        log.error(errorMsg, e);
        responseObserver.onError(Status.INTERNAL
                .withDescription(errorMsg)
                .asRuntimeException());
    }
}