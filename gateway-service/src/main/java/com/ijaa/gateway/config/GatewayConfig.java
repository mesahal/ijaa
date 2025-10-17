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
                        .path("/ijaa/api/v1/admin/feature-flags/*/enabled")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/auth/login", "/ijaa/api/v1/auth/register", "/ijaa/api/v1/auth/refresh", "/ijaa/api/v1/auth/logout")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/admin/login", "/ijaa/api/v1/admin/admins")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/locations/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service")) // Location endpoints are public
                .route(p -> p
                        .path("/ijaa/api/v1/files/users/*/profile-photo/file/**", 
                              "/ijaa/api/v1/files/users/*/cover-photo/file/**",
                              "/ijaa/api/v1/files/events/*/banner/file/**",
                              "/ijaa/api/v1/files/posts/*/media/file/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://file-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/health/**",
                              "/ijaa/test/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service")) // User service health endpoints
                .route(p -> p
                        .path("/ijaa/api/v1/events/health/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event-service")) // Event service health endpoints
                .route(p -> p
                        .path("/ijaa/api/v1/files/health/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://file-service")) // File service health endpoints
                .route(p -> p
                        .path("/ijaa/api/v1/config/health/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://config-service")) // Config service health endpoints
                .route(p -> p
                        .path("/ijaa/api/v1/discovery/health/**")
                        .filters(f -> f
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://discovery-service")) // Discovery service health endpoints
                // Protected endpoints (authentication required) - One route per service
                .route(p -> p
                        .path("/ijaa/api/v1/users/**", "/ijaa/api/v1/admin/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/events/**")
                        .filters(f -> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event-service"))
                .route(p -> p
                        .path("/ijaa/api/v1/files/**")
                        .and()
                        .not(route -> route.path("/ijaa/api/v1/files/users/*/profile-photo/file/**",
                                                "/ijaa/api/v1/files/users/*/cover-photo/file/**",
                                                "/ijaa/api/v1/files/events/*/banner/file/**",
                                                "/ijaa/api/v1/files/posts/*/media/file/**"))
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
