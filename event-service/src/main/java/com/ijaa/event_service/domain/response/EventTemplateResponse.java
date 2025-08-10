package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTemplateResponse {
    private Long id;
    private String name;
    private String createdByUsername;
    private String category;
    private Boolean isPublic;
    private Boolean isActive;
    
    // Template Content
    private String title;
    private String description;
    private String location;
    private String eventType;
    private Boolean isOnline;
    private String meetingLink;
    private Integer maxParticipants;
    private String organizerName;
    private String organizerEmail;
    private String inviteMessage;
    private String privacy;
    
    // Default Duration
    private Integer defaultDurationMinutes;
    private LocalTime defaultStartTime;
    private LocalTime defaultEndTime;
    
    // Recurring Template Settings
    private Boolean supportsRecurrence;
    private String defaultRecurrenceType;
    private Integer defaultRecurrenceInterval;
    private String defaultRecurrenceDays;
    
    // Template Usage Statistics
    private Integer usageCount;
    private Double averageRating;
    private Integer totalRatings;
    private String tags;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 