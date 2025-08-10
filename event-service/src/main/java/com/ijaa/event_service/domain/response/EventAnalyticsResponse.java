package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAnalyticsResponse {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private String organizerUsername;
    
    // Attendance Tracking
    private Integer totalInvitations;
    private Integer confirmedAttendees;
    private Integer maybeAttendees;
    private Integer declinedAttendees;
    private Integer pendingResponses;
    
    // Engagement Metrics
    private Integer totalComments;
    private Integer totalMediaUploads;
    private Integer totalReminders;
    
    // Response Time Metrics
    private LocalDateTime firstRsvpTime;
    private LocalDateTime lastRsvpTime;
    private Integer averageResponseTimeHours;
    
    // Event Performance
    private Double attendanceRate;
    private Double engagementRate;
    private Boolean isCompleted;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 