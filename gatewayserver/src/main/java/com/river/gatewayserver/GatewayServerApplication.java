package com.river.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/river/accounts/**")
                        .filters(f -> f.rewritePath("/river/accounts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker").setFallbackUri("forward:/fallback"))
                        ).uri("lb://ACCOUNTS"))
                .route(p -> p.path("/river/loans/**")
                        .filters(f -> f.rewritePath("/river/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET).setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver()))
                        ).uri("lb://LOANS"))
                .route(p -> p.path("/river/cards/**")
                        .filters(f -> f.rewritePath("/river/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET).setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver()))
                        ).uri("lb://CARDS"))
                .build();
    }

    /**
     * Creates a customizer for the ReactiveResilience4JCircuitBreakerFactory.
     * This customizer configures the default circuit breaker and time limiter settings.
     *
     * @return a customizer for the ReactiveResilience4JCircuitBreakerFactory
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        // Create a customizer that configures the default circuit breaker and time limiter settings
        return factory -> factory.configureDefault(id -> {
            // Create a Resilience4JConfigBuilder with the given id
            Resilience4JConfigBuilder builder = new Resilience4JConfigBuilder(id);

            // Configure the circuit breaker with default settings
            builder.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults());

            // Configure the time limiter with a custom timeout duration of 4 seconds
            builder.timeLimiterConfig(TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(4)) // Set the timeout duration to 4 seconds
                    .build()); // Build the time limiter config

            // Build the Resilience4JConfig
            return builder.build();
        });
    }


    /**
     * Creates a Redis-based rate limiter with default settings.
     *
     * @return a RedisRateLimiter instance
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // Create a RedisRateLimiter with default settings (1 token, 1 refill token per second, 1 burst capacity)
        return new RedisRateLimiter(1, 1, 1);
    }

    /**
     * Creates a KeyResolver that extracts the user identifier from the request headers.
     *
     * @return a KeyResolver instance
     */
    @Bean
    KeyResolver userKeyResolver() {
        // Create a KeyResolver that extracts the 'user' header from the request
        // If the header is not present, default to 'anonymous'
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }

}
