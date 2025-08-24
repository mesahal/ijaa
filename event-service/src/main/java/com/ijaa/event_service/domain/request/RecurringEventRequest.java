package com.ijaa.event_service.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringEventRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
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
    
    // Recurring Event Specific Fields
    @NotNull(message = "Recurrence type is required")
    private String recurrenceType; // DAILY, WEEKLY, MONTHLY, YEARLY
    
    @NotNull(message = "Recurrence interval is required")
    @Min(value = 1, message = "Recurrence interval must be at least 1")
    private Integer recurrenceInterval = 1;
    
    @NotNull(message = "Recurrence end date is required")
    private LocalDateTime recurrenceEndDate;
    
    private String recurrenceDays; // Comma-separated days for weekly recurrence
    
    @Min(value = 0, message = "Max occurrences cannot be negative")
    private Integer maxOccurrences = 0; // 0 means unlimited
    
    private Boolean generateInstances = true;
} 