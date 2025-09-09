package com.ijaa.event.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInvitationRequest {
    
    @NotNull(message = "Event ID is required")
    private Long eventId;
    
    @NotEmpty(message = "At least one username is required")
    private List<String> usernames;
    
    private String personalMessage; // Optional personal message
} 
