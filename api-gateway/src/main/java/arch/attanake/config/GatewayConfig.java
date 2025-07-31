package arch.attanake.config;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.config.GrpcChannelsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
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

                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .addRequestHeader("X-User-Id", "#{@jwtExtractor.getUserId()}"))
                        .uri("lb://user-service"))

                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofSeconds(1), 2, false))
                                .circuitBreaker(config -> config
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/order-fallback")))
                                .uri("lb://order-service"))

                .route("inventory-service", r -> r.path("/api/internal/inventory/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://inventory-service"))
                .route("grpc-product-service", r -> r.path("/grpc/product/**")
                        .filters(f -> f
                                .rewritePath("/grpc/product/(?<segment>.*)", "/${segment}")
                                .setRequestHeader("content-type", "application/grpc+proto")
                                .setResponseHeader("content-type", "application/grpc+proto"))
                        .metadata("preserve-host-header", "true")
                        .metadata("connect-timeout", "5000")
                        .metadata("idle-timeout", "60000")
                        .uri("lb://product-service?enableHttp2=true"))
                .build();
    }

    @Bean
    public GrpcChannelsProperties grpcChannelsProperties() {
        return new GrpcChannelsProperties();
    }


    @Bean
    public DiscoveryClientRouteDefinitionLocator discoveryRouteLocator(
            ReactiveDiscoveryClient discoveryClient,
            org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory circuitBreakerFactory) {

        return new DiscoveryClientRouteDefinitionLocator(
                discoveryClient,
                new DiscoveryLocatorProperties()
        );
    }
}
