package com.ijaa.event_service.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTemplateRequest {
    
    @NotBlank(message = "Template name is required")
    private String name;
    
    @NotNull(message = "Template category is required")
    private String category; // CONFERENCE, WORKSHOP, MEETING, etc.
    
    private Boolean isPublic = false;
    
    // Template Content
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String location;
    
    private String eventType;
    
    private Boolean isOnline = false;
    
    private String meetingLink;
    
    @NotNull(message = "Max participants is required")
    @Min(value = 1, message = "Max participants must be at least 1")
    private Integer maxParticipants;
    
    private String organizerName;
    
    private String organizerEmail;
    
    private String inviteMessage;
    
    private String privacy = "PUBLIC";
    
    // Default Duration (in minutes)
    @Min(value = 15, message = "Default duration must be at least 15 minutes")
    @Max(value = 1440, message = "Default duration cannot exceed 24 hours")
    private Integer defaultDurationMinutes = 60;
    
    private LocalTime defaultStartTime;
    
    private LocalTime defaultEndTime;
    
    // Recurring Template Settings
    private Boolean supportsRecurrence = false;
    
    private String defaultRecurrenceType; // DAILY, WEEKLY, MONTHLY, YEARLY
    
    @Min(value = 1, message = "Default recurrence interval must be at least 1")
    private Integer defaultRecurrenceInterval = 1;
    
    private String defaultRecurrenceDays; // Comma-separated days for weekly recurrence
    
    private String tags; // Comma-separated tags for search
} 