package arch.attanake.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GrpcClientConfig {

    private final EurekaClient eurekaClient;

    @Bean
    public ManagedChannel productServiceChannel() {
        try {
            log.info("Creating product service grpc channel");
            InstanceInfo instance = eurekaClient.getNextServerFromEureka("product-service", false);
            return ManagedChannelBuilder.forAddress(instance.getHostName(),
                            Integer.parseInt(instance.getMetadata().get("grpc-port")))
                    .usePlaintext()
                    .enableRetry()
                    .maxRetryAttempts(3)
                    .maxInboundMessageSize(100 * 1024 * 1024)
                    .idleTimeout(60, TimeUnit.SECONDS)
                    .keepAliveTime(30, TimeUnit.SECONDS)
                    .keepAliveTimeout(10, TimeUnit.SECONDS)
                    .build();
        }catch (Exception e) {
            log.info("Can't get grpc managed channel: {}", e.getMessage());
            return null;
        }
    }
}
