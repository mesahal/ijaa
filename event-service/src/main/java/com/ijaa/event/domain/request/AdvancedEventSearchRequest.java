package com.ijaa.event.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Advanced event search request with flexible filtering options")
public class AdvancedEventSearchRequest {

    @Schema(description = "General search text (searches title and description)", example = "alumni networking")
    private String query;
    
    @Schema(description = "Filter by event location", example = "IIT Campus")
    private String location;
    
    @Schema(description = "Filter by event type", example = "MEETING", allowableValues = {"MEETING", "WEBINAR", "NETWORKING", "WORKSHOP", "CONFERENCE"})
    private String eventType;
    
    @Schema(description = "Filter events starting after this date", example = "2024-12-01T00:00:00")
    private LocalDateTime startDate;
    
    @Schema(description = "Filter events ending before this date", example = "2024-12-31T23:59:59")
    private LocalDateTime endDate;
    
    @Schema(description = "Filter by organizer name", example = "John Doe")
    private String organizerName;
    
    @Schema(description = "Filter by online/offline events", example = "true")
    private Boolean isOnline;
    
    @Schema(description = "Filter by privacy setting", example = "PUBLIC", allowableValues = {"PUBLIC", "PRIVATE", "INVITE_ONLY"})
    private String privacy;
    
    @Schema(description = "Minimum participant count", example = "10")
    private Integer minParticipants;
    
    @Schema(description = "Maximum participant count", example = "100")
    private Integer maxParticipants;
    
    @Schema(description = "Filter events with comments (deprecated - comments now post-based)", example = "false")
    private Boolean hasComments;
    
    @Schema(description = "Filter events with media (deprecated)", example = "false")
    private Boolean hasMedia;
    
    @Schema(description = "Future feature: Filter by tags")
    private List<String> tags;
    
    @Schema(description = "Sort field", example = "start_date", allowableValues = {"start_date", "created_at", "popularity", "distance"})
    private String sortBy;
    
    @Schema(description = "Sort order", example = "asc", allowableValues = {"asc", "desc"})
    private String sortOrder;
    
    @Schema(description = "Page number for pagination", example = "0", defaultValue = "0")
    private Integer page = 0;
    
    @Schema(description = "Page size for pagination", example = "20", defaultValue = "20")
    private Integer size = 20;
    
    @Schema(description = "Filter only upcoming events (starts in the future)", example = "true", defaultValue = "false")
    private Boolean upcomingOnly = false;
    
    @Schema(description = "Optional limit for results (alternative to pagination)", example = "10")
    private Integer limit;
    
    @Schema(description = "Include recommendations (future feature)", example = "false", defaultValue = "false")
    private Boolean includeRecommendations = false;
    
    @Schema(description = "User location for distance-based search (future feature)", example = "Dhaka")
    private String userLocation;
} 
