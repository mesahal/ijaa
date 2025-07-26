package com.ijaa.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.common.exceptions.UserContextException;
import com.ijaa.user.domain.entity.CurrentUserContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class BaseService {

    private final ObjectMapper objectMapper;

    /**
     * Extracts the current user context from the request header set by the gateway service
     *
     * @return CurrentUserContext object containing user information
     * @throws UserContextException if user context cannot be extracted or parsed
     */
    public CurrentUserContext getCurrentUserContext() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String base64UserContext = request.getHeader("X-USER_ID");

            if (base64UserContext == null || base64UserContext.trim().isEmpty()) {
                throw new UserContextException("User context not found in request headers");
            }

            return decodeUserContext(base64UserContext);

        } catch (UserContextException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to extract current user context: {}", e.getMessage(), e);
            throw new UserContextException("Unable to determine current user context", e);
        }
    }

    /**
     * Gets the current username from the user context
     *
     * @return username of the currently authenticated user
     */
    public String getCurrentUsername() {
        CurrentUserContext userContext = getCurrentUserContext();
        return userContext.getUsername();
    }

    /**
     * Checks if a user context exists in the current request
     *
     * @return true if user context is available, false otherwise
     */
    public boolean hasUserContext() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String base64UserContext = request.getHeader("X-USER_ID");
            return base64UserContext != null && !base64UserContext.trim().isEmpty();
        } catch (Exception e) {
            log.debug("No user context available: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Gets the current HTTP request from RequestContextHolder
     *
     * @return HttpServletRequest object
     * @throws UserContextException if request context is not available
     */
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new UserContextException("No request context available");
        }

        return requestAttributes.getRequest();
    }

    /**
     * Decodes the Base64-encoded user context from the header
     *
     * @param base64UserContext Base64-encoded JSON string
     * @return CurrentUserContext object
     * @throws UserContextException if decoding or parsing fails
     */
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
            throw new UserContextException("Invalid user context encoding", e);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse user context JSON: {}", e.getMessage());
            throw new UserContextException("Invalid user context format", e);
        }
    }
}

