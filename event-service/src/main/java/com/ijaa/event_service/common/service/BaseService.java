package com.ijaa.event_service.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event_service.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseService {

    private final ObjectMapper objectMapper;

    protected String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    protected boolean isCurrentUser(String username) {
        String currentUsername = getCurrentUsername();
        return currentUsername != null && currentUsername.equals(username);
    }

    protected boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
        }
        return false;
    }
}
