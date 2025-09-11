package com.ijaa.gateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;


@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured = request ->
            Stream.of(
                    "/ijaa/api/v1/user/signup",
                    "/ijaa/api/v1/user/signin",
                    "/ijaa/api/v1/user/refresh",
                    "/ijaa/api/v1/user/logout",
                    "/ijaa/api/v1/user/admin/login",
                    "/ijaa/api/v1/user/admin/signup",
                    "/ijaa/api/v1/user/admin/feature-flags/*/enabled",
                    "/ijaa/api/v1/file/users/*/profile-photo/file/**",
                    "/ijaa/api/v1/file/users/*/cover-photo/file/**",
                    "/ijaa/api/v1/file/events/*/banner/file/**",
                    "/ijaa/actuator/**",
                    "/ijaa/api/v1/health/**",
                    "/ijaa/api/v1/health/user/**",
                    "/ijaa/api/v1/health/event/**",
                    "/ijaa/api/v1/health/file/**",
                    "/ijaa/test/**"
            )
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
