package arch.attanake.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel inventoryServiceChannel(
            @Value("${grpc.inventory-service.host:localhost}") String host,
            @Value("${grpc.inventory-service.port:9090}") int port) {

        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }
}