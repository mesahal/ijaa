package com.ijaa.user.domain.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardStatsResponse {
    // User statistics
    private Long totalUsers;
    private Long activeUsers;
    private Long blockedUsers;
    
    // Admin statistics
    private Long totalAdmins;
    private Long activeAdmins;
    
    // Additional data (placeholder)
    private List<Map<String, Object>> topBatches;
    private List<Map<String, Object>> recentActivities;
} 