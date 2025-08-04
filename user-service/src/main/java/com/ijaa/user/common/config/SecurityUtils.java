package com.ijaa.user.common.config;

import com.ijaa.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final AdminService adminService;

    /**
     * Custom security expression to check if this is the first admin creation
     * This allows the first admin to be created without authentication
     */
    public boolean isFirstAdmin() {
        return adminService.isFirstAdmin();
    }
} 