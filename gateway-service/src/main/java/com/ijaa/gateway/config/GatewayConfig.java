package com.ijaa.gateway.config;

import com.ijaa.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator ijaaRouteConfig(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()
                // Public endpoints (no authentication required)
                .route(p -> p
                        .path("/ijaa/api/v1/user/admin/feature-flags/*/enabled")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/user/signin", "/ijaa/api/v1/user/signup", "/ijaa/api/v1/user/refresh", "/ijaa/api/v1/user/logout")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/user/admin/login", "/ijaa/api/v1/user/admin/signup")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/file/users/*/profile-photo/file/**", 
                              "/ijaa/api/v1/file/users/*/cover-photo/file/**",
                              "/ijaa/api/v1/file/events/*/banner/file/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://file-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/user/health/**",
                              "/ijaa/api/v1/event/health/**", 
                              "/ijaa/api/v1/file/health/**",
                              "/ijaa/api/v1/config/health/**",
                              "/ijaa/api/v1/discovery/health/**",
                              "/ijaa/test/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service")) // Health and test endpoints can be routed to any service
                // Protected endpoints (authentication required) - One route per service
                .route(p -> p
                        .path("/ijaa/api/v1/user/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/event/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p -> p
                        .path("/ijaa/api/v1/file/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://file-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/config/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://config-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/discovery/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://discovery-service"))
                .build();
    }
}
