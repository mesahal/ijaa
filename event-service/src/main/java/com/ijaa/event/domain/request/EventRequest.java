package com.ijaa.event.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    
    @NotBlank(message = "Event title is required")
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
    
    @NotNull(message = "Maximum participants is required")
    @Min(value = 1, message = "Maximum participants must be at least 1")
    private Integer maxParticipants;
    
    @NotBlank(message = "Organizer name is required")
    private String organizerName;
    
    @Email(message = "Organizer email must be valid")
    @NotBlank(message = "Organizer email is required")
    private String organizerEmail;
    
    private String privacy = "PUBLIC"; // PUBLIC, PRIVATE, INVITE_ONLY
    
    private String inviteMessage; // Default message for invitations
} 
