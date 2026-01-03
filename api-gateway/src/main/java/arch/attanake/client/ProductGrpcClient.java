package arch.attanake.client;

import arch.attanake.controller.dto.ProductDto;
import arch.attanake.grpc.ProductListRequest;
import arch.attanake.grpc.ProductRequest;
import arch.attanake.grpc.ProductResponse;
import arch.attanake.grpc.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProductGrpcClient {

    private final ProductServiceGrpc.ProductServiceBlockingStub stub;

    public ProductGrpcClient(@Lazy ManagedChannel productServiceChannel) {
        this.stub = ProductServiceGrpc.newBlockingStub(productServiceChannel);
    }

    public ProductDto getProduct(String productId) {
        try {
            ProductResponse response = stub
                    .withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getProduct(
                            ProductRequest.newBuilder()
                                    .setId(productId)
                                    .build()
                    );

            return map(response);

        } catch (Exception e) {
            log.error("Failed to fetch product {}", productId, e);
            throw new IllegalStateException("Product service is unavailable", e);
        }
    }

    public Flux<ProductDto> getProducts(List<String> productIds) {
        return Flux.create(sink -> {
            try {
                Iterator<ProductResponse> iterator =
                        stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                                .getProducts(
                                        ProductListRequest.newBuilder()
                                                .addAllIds(productIds)
                                                .build()
                                );

                while (iterator.hasNext()) {
                    sink.next(map(iterator.next()));
                }

                sink.complete();
            } catch (Exception e) {
                log.error("Failed to fetch products {}", productIds, e);
                sink.error(new IllegalStateException("Product service is unavailable", e));
            }
        });
    }

    private ProductDto map(ProductResponse r) {
        return new ProductDto(
                r.getId(),
                r.getName(),
                r.getPrice(),
                r.getAvailable()
        );
    }
}
