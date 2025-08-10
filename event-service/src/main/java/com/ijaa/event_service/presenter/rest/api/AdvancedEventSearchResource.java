package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event_service.domain.response.EventResponse;
import com.ijaa.event_service.service.AdvancedEventSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/events/search")
@RequiredArgsConstructor
@Tag(name = "Advanced Event Search", description = "Advanced event search and recommendation APIs")
public class AdvancedEventSearchResource {

    private final AdvancedEventSearchService advancedEventSearchService;

    @PostMapping("/advanced")
    @Operation(summary = "Advanced event search", description = "Search events with multiple filters and criteria")
    public ResponseEntity<ApiResponse<PagedResponse<EventResponse>>> searchEvents(
            @RequestBody AdvancedEventSearchRequest request) {
        
        PagedResponse<EventResponse> response = advancedEventSearchService.searchEvents(request);
        
        return ResponseEntity.ok(new ApiResponse<>("Events found successfully", "200", response));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get event recommendations", description = "Get personalized event recommendations for the user")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventRecommendations(
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        List<EventResponse> response = advancedEventSearchService.getEventRecommendations(username);
        
        return ResponseEntity.ok(new ApiResponse<>("Event recommendations retrieved successfully", "200", response));
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending events", description = "Get trending events based on engagement")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getTrendingEvents(
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "10") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getTrendingEvents(limit);
        
        return ResponseEntity.ok(new ApiResponse<>("Trending events retrieved successfully", "200", response));
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Get events by location", description = "Get events in a specific location")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByLocation(
            @PathVariable String location,
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "10") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getEventsByLocation(location, limit);
        
        return ResponseEntity.ok(new ApiResponse<>("Events by location retrieved successfully", "200", response));
    }

    @GetMapping("/organizer/{organizerName}")
    @Operation(summary = "Get events by organizer", description = "Get events organized by a specific person")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByOrganizer(
            @PathVariable String organizerName,
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "10") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getEventsByOrganizer(organizerName, limit);
        
        return ResponseEntity.ok(new ApiResponse<>("Events by organizer retrieved successfully", "200", response));
    }

    @GetMapping("/high-engagement")
    @Operation(summary = "Get high engagement events", description = "Get events with high engagement (comments, media, participants)")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getHighEngagementEvents(
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "10") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getHighEngagementEvents(limit);
        
        return ResponseEntity.ok(new ApiResponse<>("High engagement events retrieved successfully", "200", response));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming events", description = "Get upcoming events with optional filters")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getUpcomingEvents(
            @Parameter(description = "Location filter") @RequestParam(required = false) String location,
            @Parameter(description = "Event type filter") @RequestParam(required = false) String eventType,
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "10") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getUpcomingEvents(location, eventType, limit);
        
        return ResponseEntity.ok(new ApiResponse<>("Upcoming events retrieved successfully", "200", response));
    }

    @GetMapping("/similar/{eventId}")
    @Operation(summary = "Get similar events", description = "Get events similar to a specific event")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getSimilarEvents(
            @PathVariable Long eventId,
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "5") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getSimilarEvents(eventId, limit);
        
        return ResponseEntity.ok(new ApiResponse<>("Similar events retrieved successfully", "200", response));
    }

    private String extractUsername(String userContext) {
        // This is a simplified implementation
        // In a real app, you'd decode the JWT token or user context
        return userContext; // Assuming userContext contains the username
    }
} 