package com.ijaa.event_service.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAnalyticsRequest {
    
    @NotNull(message = "Event ID is required")
    private Long eventId;
    
    private String eventTitle;
    
    private String organizerUsername;
    
    // Attendance Tracking
    @Min(value = 0, message = "Total invitations cannot be negative")
    private Integer totalInvitations = 0;
    
    @Min(value = 0, message = "Confirmed attendees cannot be negative")
    private Integer confirmedAttendees = 0;
    
    @Min(value = 0, message = "Maybe attendees cannot be negative")
    private Integer maybeAttendees = 0;
    
    @Min(value = 0, message = "Declined attendees cannot be negative")
    private Integer declinedAttendees = 0;
    
    @Min(value = 0, message = "Pending responses cannot be negative")
    private Integer pendingResponses = 0;
    
    // Engagement Metrics
    @Min(value = 0, message = "Total comments cannot be negative")
    private Integer totalComments = 0;
    
    @Min(value = 0, message = "Total media uploads cannot be negative")
    private Integer totalMediaUploads = 0;
    
    @Min(value = 0, message = "Total reminders cannot be negative")
    private Integer totalReminders = 0;
    
    // Event Performance
    private Double attendanceRate = 0.0;
    
    private Double engagementRate = 0.0;
    
    private Boolean isCompleted = false;
    
    private LocalDateTime eventStartDate;
    
    private LocalDateTime eventEndDate;
} 