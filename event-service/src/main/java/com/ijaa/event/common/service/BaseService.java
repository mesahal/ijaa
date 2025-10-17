package com.ijaa.event.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class BaseService {

    private final ObjectMapper objectMapper;

    protected String getCurrentUsername() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String base64UserContext = request.getHeader("X-USER_ID");

            if (base64UserContext == null || base64UserContext.trim().isEmpty()) {
                log.warn("User context not found in request headers");
                return null;
            }

            CurrentUserContext userContext = decodeUserContext(base64UserContext);
            return userContext.getUsername();

        } catch (Exception e) {
            log.error("Failed to extract current username: {}", e.getMessage(), e);
            return null;
        }
    }

    protected boolean isCurrentUser(String username) {
        String currentUsername = getCurrentUsername();
        return currentUsername != null && currentUsername.equals(username);
    }

    protected boolean hasRole(String role) {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String base64UserContext = request.getHeader("X-USER_ID");

            if (base64UserContext == null || base64UserContext.trim().isEmpty()) {
                return false;
            }

            CurrentUserContext userContext = decodeUserContext(base64UserContext);
            return role.equals(userContext.getRole());

        } catch (Exception e) {
            log.error("Failed to check role: {}", e.getMessage(), e);
            return false;
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

    protected String getCurrentUserId() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String base64UserContext = request.getHeader("X-USER_ID");
            
            log.info("X-USER_ID header value: {}", base64UserContext);

            if (base64UserContext == null || base64UserContext.trim().isEmpty()) {
                log.warn("User context not found in request headers");
                return null;
            }

            CurrentUserContext userContext = decodeUserContext(base64UserContext);
            log.info("Decoded user context: username={}, userId={}, role={}", 
                    userContext.getUsername(), userContext.getUserId(), userContext.getRole());
            return userContext.getUserId();

        } catch (Exception e) {
            log.error("Failed to extract current user ID: {}", e.getMessage(), e);
            return null;
        }
    }

    // Inner class for user context
    public static class CurrentUserContext {
        private String username;
        private String userId;
        private String userType;
        private String role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
