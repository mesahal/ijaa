package com.ijaa.user.domain.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardStatsResponse {
    private Long totalUsers;
    private Long activeUsers;
    private Long blockedUsers;
    private Long totalEvents;
    private Long activeEvents;
    private Long totalAnnouncements;
    private Long pendingReports;
    private List<Map<String, Object>> topBatches;
    private List<Map<String, Object>> recentActivities;
} 