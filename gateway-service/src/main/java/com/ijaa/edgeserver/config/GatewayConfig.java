package com.ijaa.edgeserver.config;

import com.ijaa.edgeserver.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator walletRouteConfig(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()
                .route(p-> p
                        .path("/ijaa/api/v1/user/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://USERS"))
                // Admin routes - no authentication filter for login/signup
                .route(p-> p
                        .path("/ijaa/api/v1/admin/**")
                        .filters(f-> f
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://USERS"))
                .build();
    }
}
