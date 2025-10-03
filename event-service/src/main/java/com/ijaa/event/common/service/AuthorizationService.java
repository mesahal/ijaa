package com.ijaa.event.common.service;

import com.ijaa.event.common.service.BaseService.CurrentUserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

    private final ObjectMapper objectMapper;

    /**
     * Check if the current user has the specified role
     */
    public boolean hasRole(String role) {
        try {
            CurrentUserContext userContext = getCurrentUserContext();
            if (userContext == null) {
                return false;
            }
            return role.equals(userContext.getRole());
        } catch (Exception e) {
            log.error("Failed to check role: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Check if the current user is authenticated
     */
    public boolean isAuthenticated() {
        try {
            CurrentUserContext userContext = getCurrentUserContext();
            return userContext != null && userContext.getUsername() != null;
        } catch (Exception e) {
            log.error("Failed to check authentication: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get the current username
     */
    public String getCurrentUsername() {
        try {
            CurrentUserContext userContext = getCurrentUserContext();
            return userContext != null ? userContext.getUsername() : null;
        } catch (Exception e) {
            log.error("Failed to get current username: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get the current user context from the gateway-provided header
     */
    private CurrentUserContext getCurrentUserContext() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String base64UserContext = request.getHeader("X-USER_ID");

            if (base64UserContext == null || base64UserContext.trim().isEmpty()) {
                log.warn("User context not found in request headers");
                return null;
            }

            return decodeUserContext(base64UserContext);
        } catch (Exception e) {
            log.error("Failed to extract user context: {}", e.getMessage(), e);
            return null;
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new RuntimeException("No request context available");
        }

        return requestAttributes.getRequest();
    }

    private CurrentUserContext decodeUserContext(String base64UserContext) {
        try {
            // Decode from Base64
            byte[] decodedBytes = Base64.getUrlDecoder().decode(base64UserContext);
            String jsonUserContext = new String(decodedBytes, StandardCharsets.UTF_8);

            log.debug("Decoded user context JSON: {}", jsonUserContext);

            // Parse JSON to CurrentUserContext object
            return objectMapper.readValue(jsonUserContext, CurrentUserContext.class);

        } catch (IllegalArgumentException e) {
            log.error("Invalid Base64 encoding in user context header: {}", base64UserContext);
            throw new RuntimeException("Invalid user context encoding", e);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse user context JSON: {}", e.getMessage());
            throw new RuntimeException("Invalid user context format", e);
        }
    }
}
