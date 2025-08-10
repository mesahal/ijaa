package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarIntegrationResponse {
    private Long id;
    private String username;
    private String calendarType;
    private String calendarName;
    private String calendarUrl;
    private String calendarId;
    private Boolean isActive;
    private Boolean syncToExternal;
    private Boolean syncFromExternal;
    private Boolean syncRecurringEvents;
    private Boolean syncReminders;
    private String lastSyncError;
    private LocalDateTime lastSyncTime;
    private Integer syncFrequencyHours;
    private LocalDateTime tokenExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 