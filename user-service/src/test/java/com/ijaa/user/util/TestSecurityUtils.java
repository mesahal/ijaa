package com.ijaa.user.util;

import com.ijaa.user.domain.entity.CurrentUserContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestSecurityUtils {

    /**
     * Creates a mock JWT token for testing purposes
     */
    public static String createMockJwtToken(String username) {
        // This is a mock token for testing - in real scenarios, use JWTService
        return "mock.jwt.token." + username + ".test";
    }

    /**
     * Creates a mock user context for testing
     */
    public static CurrentUserContext createMockUserContext(String username) {
        CurrentUserContext context = new CurrentUserContext();
        context.setUsername(username);
        return context;
    }

    /**
     * Sets up security context with a mock user
     */
    public static void setupSecurityContext(String username) {
        CurrentUserContext userContext = createMockUserContext(username);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userContext, null, null);
        
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    /**
     * Clears the security context
     */
    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
} 