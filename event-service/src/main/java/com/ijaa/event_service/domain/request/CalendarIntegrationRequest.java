package com.ijaa.event_service.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarIntegrationRequest {
    
    @NotBlank(message = "Calendar name is required")
    private String calendarName;
    
    @NotNull(message = "Calendar type is required")
    private String calendarType; // GOOGLE_CALENDAR, OUTLOOK_CALENDAR, APPLE_CALENDAR, OTHER
    
    private String calendarUrl;
    
    private String accessToken;
    
    private String refreshToken;
    
    private LocalDateTime tokenExpiry;
    
    private String calendarId;
    
    private Boolean isActive = true;
    
    private Boolean syncToExternal = false;
    
    private Boolean syncFromExternal = false;
    
    private Boolean syncRecurringEvents = false;
    
    private Boolean syncReminders = false;
    
    @Min(value = 1, message = "Sync frequency must be at least 1 hour")
    private Integer syncFrequencyHours = 24;
} 