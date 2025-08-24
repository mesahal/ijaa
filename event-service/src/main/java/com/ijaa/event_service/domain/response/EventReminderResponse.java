package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventReminderResponse {

    private Long id;
    private Long eventId;
    private String username;
    private LocalDateTime reminderTime;
    private String reminderType;
    private Boolean isSent;
    private Boolean isActive;
    private String customMessage;
    private String channel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 