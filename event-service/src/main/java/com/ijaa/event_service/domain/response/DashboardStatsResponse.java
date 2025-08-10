package com.ijaa.event_service.domain.response;

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
    
    // Event statistics
    private Long totalEvents;
    private Long activeEvents;
    
    // Announcement statistics
    private Long totalAnnouncements;
    private Long activeAnnouncements;
    
    // Report statistics
    private Long totalReports;
    private Long pendingReports;
    
    // Additional data (placeholder)
    private List<Map<String, Object>> topBatches;
    private List<Map<String, Object>> recentActivities;
} 