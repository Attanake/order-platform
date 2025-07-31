package arch.attanake.config;

import arch.attanake.grpc.InventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.inventory-service.host:inventory-service}")
    private String inventoryHost;

    @Value("${grpc.inventory-service.port:9090}")
    private int inventoryPort;

    @Bean
    public ManagedChannel inventoryChannel() {
        return ManagedChannelBuilder.forAddress(inventoryHost, inventoryPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub(ManagedChannel channel) {
        return InventoryServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
    }
}