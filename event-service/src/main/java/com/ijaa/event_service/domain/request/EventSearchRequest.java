package com.ijaa.event_service.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchRequest {
    
    private String location;
    private String eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isOnline;
    private String organizerName;
    private String title; // Search in title
    private String description; // Search in description
    private Integer page = 0;
    private Integer size = 10;
} 