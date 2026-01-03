package arch.attanake.config;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.config.GrpcChannelsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcProperties.class)
public class GatewayConfig {

    private final RedisRateLimiter redisRateLimiter;
    private final KeyResolver userKeyResolver;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(userKeyResolver))
                                .circuitBreaker(c -> c
                                        .setName("authCB")
                                        .setFallbackUri("forward:/auth-fallback")))
                        .uri("lb://auth-service"))

                .route("user-service", r -> r.path("/api/user/**")
                        .filters(f -> f
                                .stripPrefix(1))
                        .uri("lb://user-service"))

                .route("order-service", r -> r.path("/api/order/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofSeconds(1), 2, false))
                                .circuitBreaker(config -> config
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/order-fallback")))
                                .uri("lb://order-service"))

                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://inventory-service"))

                .route("product-service", r -> r
                        .path("/api/product/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://product-service")
                )

                .route("notification-service", r -> r
                        .path("/api/notification/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://notification-service")
                )

                .route("schema-service", r -> r
                        .path("/api/schema/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://schema-service")
                )
                .build();
    }

    @Bean
    public GrpcChannelsProperties grpcChannelsProperties() {
        return new GrpcChannelsProperties();
    }
}
