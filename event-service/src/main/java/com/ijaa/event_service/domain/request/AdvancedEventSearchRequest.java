package com.ijaa.event_service.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedEventSearchRequest {

    private String query; // General search query
    private String location;
    private String eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String organizerName;
    private Boolean isOnline;
    private String privacy;
    private Integer minParticipants;
    private Integer maxParticipants;
    private Boolean hasComments;
    private Boolean hasMedia;
    private List<String> tags; // For future tag system
    private String sortBy; // CREATED_AT, START_DATE, POPULARITY, DISTANCE
    private String sortOrder; // ASC, DESC
    private Integer page = 0;
    private Integer size = 20;
    private Boolean includeRecommendations = false;
    private String userLocation; // For distance-based recommendations
} 