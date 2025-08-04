package com.ijaa.edgeserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.edgeserver.domain.dto.CurrentUserContext;
import com.ijaa.edgeserver.domain.enums.JwtClaimsEnum;
import com.ijaa.edgeserver.exceptions.MissingAuthorizationHeaderException;
import com.ijaa.edgeserver.utils.JwtUtil;
import com.ijaa.edgeserver.utils.SerializationUtils;
import com.ijaa.edgeserver.validator.RouteValidator;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthenticationFilter(RouteValidator routeValidator, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new MissingAuthorizationHeaderException("Missing Authorization Header");
                }

                String token = authHeader.substring(7);
                ServerHttpRequest request = exchange.getRequest(); // initialize with default request

                try {
                    jwtUtil.validateToken(token,jwtSecret);
                    CurrentUserContext currentUserContext = prepareCurrentContext(token);

                    String jsonCurrentUserContext = toJson(currentUserContext);
                    String base64CurentUserContext = SerializationUtils.toBase64(jsonCurrentUserContext.getBytes(StandardCharsets.UTF_8));

                    request = exchange.getRequest().mutate()
                            .header("X-USER_ID",base64CurentUserContext)
                            .build();
                } catch (ExpiredJwtException e) {
                    throw new RuntimeException("Expired JWT token");
                }
                catch (Exception e) {
                    return exchange.getResponse().setComplete();
                }
                return chain.filter(exchange.mutate().request(request).build());

            }
            return chain.filter(exchange);
        };
    }

    private CurrentUserContext prepareCurrentContext(String token) {
        try {
            String userType = jwtUtil.extractUserType(token, jwtSecret);
            String role = jwtUtil.extractRole(token, jwtSecret);
            
            CurrentUserContext currentUserContext = new CurrentUserContext();
            
            if ("ADMIN".equals(userType)) {
                String email = jwtUtil.extractEmail(token, jwtSecret);
                currentUserContext.setUsername(email);
                currentUserContext.setUserType("ADMIN");
                currentUserContext.setRole(role);
            } else {
                String username = jwtUtil.extractUsername(token, jwtSecret);
                currentUserContext.setUsername(username);
                currentUserContext.setUserType("USER");
                currentUserContext.setRole(role);
            }
            
            return currentUserContext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String toJson(CurrentUserContext currentUserContext) {
        String jsonCurrentUserContext = null;
        try {
            jsonCurrentUserContext = objectMapper.writeValueAsString(currentUserContext);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonCurrentUserContext;
    }

    public static class Config {
    }
}
