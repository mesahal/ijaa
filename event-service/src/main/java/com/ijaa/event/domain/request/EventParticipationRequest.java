package com.ijaa.event.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipationRequest {
    
    @NotNull(message = "Event ID is required")
    private Long eventId;
    
    @NotNull(message = "Participation status is required")
    private String status; // GOING, MAYBE, NOT_GOING
    
    private String message; // Optional message
} 
