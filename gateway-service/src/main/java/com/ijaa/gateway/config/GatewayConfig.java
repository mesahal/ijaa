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
                // User service routes (excluding events which are handled by event service)
                .route(p-> p
                        .path("/ijaa/api/v1/user/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                // Feature flag status check - public endpoint (no authentication required)
                .route(p-> p
                        .path("/ijaa/api/v1/admin/feature-flags/*/enabled")
                        .filters(f-> f
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                // Admin routes - apply authentication filter for protected endpoints (excluding feature flag status)
                .route(p-> p
                        .path("/ijaa/api/v1/admin/**")
                        .and()
                        .not(route -> route.path("/ijaa/api/v1/admin/feature-flags/*/enabled"))
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                // Event service routes
                .route(p-> p
                        .path("/ijaa/api/v1/events/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                // User events routes (for user-specific event operations)
                .route(p-> p
                        .path("/ijaa/api/v1/user/events/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                // User recurring events routes
                .route(p-> p
                        .path("/ijaa/api/v1/user/recurring-events/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-participations/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-invitations/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-comments/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-media/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-reminders/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/recurring-events/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-templates/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/event-analytics/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                .route(p-> p
                        .path("/ijaa/api/v1/calendar-integrations/**")
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://event"))
                // File service routes - for user profile and cover photo management
                .route(p-> p
                        .path("/ijaa/api/v1/users/**")
                        .and()
                        .not(route -> route.path("/ijaa/api/v1/users/*/profile-photo/file/**", "/ijaa/api/v1/users/*/cover-photo/file/**"))
                        .filters(f-> f
                                .filter(filter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://file-service"))
                // File serving routes - public endpoints for serving image files
                .route(p-> p
                        .path("/ijaa/api/v1/users/*/profile-photo/file/**", "/ijaa/api/v1/users/*/cover-photo/file/**")
                        .filters(f-> f
                                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://file-service"))
                .build();
    }
}
