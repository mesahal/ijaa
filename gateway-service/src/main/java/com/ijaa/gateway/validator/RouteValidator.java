package com.ijaa.gateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.stream.Stream;


@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured = request ->
            Stream.of(
                    // Authentication endpoints (public)
                    "/ijaa/api/v1/auth/login",
                    "/ijaa/api/v1/auth/register", 
                    "/ijaa/api/v1/auth/refresh",
                    "/ijaa/api/v1/auth/logout",
                    // Admin authentication endpoints (public)
                    "/ijaa/api/v1/admin/login",
                    "/ijaa/api/v1/admin/admins",
                    // Feature flag status check (public)
                    "/ijaa/api/v1/admin/feature-flags/*/enabled",
                    // Location endpoints (public)
                    "/ijaa/api/v1/locations/**",
                    // File serving endpoints (public)
                    "/ijaa/api/v1/files/users/*/profile-photo/file/**",
                    "/ijaa/api/v1/files/users/*/cover-photo/file/**",
                    "/ijaa/api/v1/files/events/*/banner/file/**",
                    // Health check endpoints (public)
                    "/ijaa/api/v1/health/**",
                    "/ijaa/api/v1/events/health/**",
                    "/ijaa/api/v1/files/health/**",
                    "/ijaa/api/v1/config/health/**",
                    "/ijaa/api/v1/discovery/health/**",
                    // Actuator and test endpoints (public)
                    "/ijaa/actuator/**",
                    "/ijaa/test/**"
            )
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
