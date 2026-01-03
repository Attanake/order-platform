package arch.attanake.controller;

import arch.attanake.client.ProductGrpcClient;
import arch.attanake.controller.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
@RestController
@RequestMapping("/grpc-api/products")
@RequiredArgsConstructor
public class GrpcProductController {

    private final ProductGrpcClient grpcClient;

    @GetMapping()
    public Mono<ProductDto> getProduct(@RequestParam(name = "productId") String id) {
        return Mono.fromCallable(() -> grpcClient.getProduct(id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/batch")
    public Flux<ProductDto> getProducts(@RequestParam(name = "productIdList") List<String> ids) {
        return grpcClient.getProducts(ids);
    }
}


