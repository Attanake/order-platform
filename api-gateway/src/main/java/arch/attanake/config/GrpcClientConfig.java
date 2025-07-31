package arch.attanake.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class GrpcClientConfig {

    private final EurekaClient eurekaClient;

    @Bean
    public ManagedChannel productServiceChannel() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("product-service", false);
        return ManagedChannelBuilder.forAddress(instance.getHostName(), instance.getPort())
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .maxInboundMessageSize(100 * 1024 * 1024)
                .idleTimeout(60, TimeUnit.SECONDS)
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(10, TimeUnit.SECONDS)
                .build();
    }
}
