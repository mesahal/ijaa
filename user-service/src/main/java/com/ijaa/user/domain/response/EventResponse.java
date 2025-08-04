package com.ijaa.user.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
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
    private String createdByUsername; // Username of the user who created the event
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 