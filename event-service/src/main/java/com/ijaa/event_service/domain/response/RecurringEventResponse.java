package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringEventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String eventType;
    private Boolean active;
    private Boolean isOnline;
    private String meetingLink;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String organizerName;
    private String organizerEmail;
    private String createdByUsername;
    private String privacy;
    private String inviteMessage;
    
    // Recurring Event Specific Fields
    private String recurrenceType;
    private Integer recurrenceInterval;
    private LocalDateTime recurrenceEndDate;
    private String recurrenceDays;
    private Integer maxOccurrences;
    private Boolean generateInstances;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 