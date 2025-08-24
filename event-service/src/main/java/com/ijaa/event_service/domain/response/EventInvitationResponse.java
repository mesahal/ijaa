package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInvitationResponse {
    private Long id;
    private Long eventId;
    private String invitedUsername;
    private String invitedByUsername;
    private String personalMessage;
    private Boolean isRead;
    private Boolean isResponded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 