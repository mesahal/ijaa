package com.ijaa.edgeserver.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured = request ->
            Stream.of("/ijaa/api/v1/user/signup","/ijaa/api/v1/user/signin")
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
