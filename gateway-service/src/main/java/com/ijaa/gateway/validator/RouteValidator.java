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
                    "/ijaa/api/v1/admin/login",
                    "/ijaa/api/v1/admin/signup",
                    "/ijaa/api/v1/users",
                    "/ijaa/actuator/**"
            )
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
