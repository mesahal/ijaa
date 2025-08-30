package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.common.annotation.RequiresFeature;
import com.ijaa.event_service.common.utils.FeatureFlagUtils;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event_service.domain.response.EventResponse;
import com.ijaa.event_service.service.AdvancedEventSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/events/advanced-search")
@RequiredArgsConstructor
@Tag(name = "Advanced Event Search", description = "APIs for advanced event search with multiple filters")
public class AdvancedEventSearchResource {

    private final AdvancedEventSearchService advancedEventSearchService;
    private final FeatureFlagUtils featureFlagUtils;

    @PostMapping("/advanced")
    @RequiresFeature("search.advanced-filters")
    @Operation(
        summary = "Advanced Event Search",
        description = "Search events with multiple filters and criteria including location, date range, event type, and more",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Advanced search criteria",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdvancedEventSearchRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Location and Date Search",
                        summary = "Search events by location and date range",
                        value = """
                            {
                                "location": "IIT Campus",
                                "startDate": "2024-12-01T00:00:00",
                                "endDate": "2024-12-31T23:59:59",
                                "eventType": "MEETING",
                                "isOnline": false,
                                "page": 0,
                                "size": 10
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Online Events Search",
                        summary = "Search for online events",
                        value = """
                            {
                                "isOnline": true,
                                "eventType": "WEBINAR",
                                "startDate": "2024-12-01T00:00:00",
                                "endDate": "2024-12-31T23:59:59",
                                "page": 0,
                                "size": 20
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Events found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Events found successfully",
                                "code": "200",
                                "data": {
                                    "content": [
                                        {
                                            "id": 1,
                                            "title": "Alumni Meet 2024",
                                            "description": "Annual alumni gathering",
                                            "startDate": "2024-12-25T18:00:00",
                                            "endDate": "2024-12-25T22:00:00",
                                            "location": "IIT Campus",
                                            "eventType": "MEETING",
                                            "active": true,
                                            "isOnline": false,
                                            "meetingLink": null,
                                            "maxParticipants": 100,
                                            "currentParticipants": 45,
                                            "organizerName": "John Doe",
                                            "organizerEmail": "john@example.com",
                                            "createdByUsername": "johndoe",
                                            "privacy": "PUBLIC",
                                            "inviteMessage": "Join us for the annual alumni meet",
                                            "createdAt": "2024-12-01T10:00:00",
                                            "updatedAt": "2024-12-01T10:00:00"
                                        }
                                    ],
                                    "totalElements": 1,
                                    "totalPages": 1,
                                    "currentPage": 0,
                                    "size": 10
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid search criteria",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid search criteria provided",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Missing or invalid token",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Unauthorized",
                        value = """
                            {
                                "message": "Missing Authorization Header",
                                "code": "401",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<PagedResponse<EventResponse>>> searchEvents(
            @RequestBody AdvancedEventSearchRequest request) {
        
        log.info("Processing advanced search request with filters: {}", request);
        
        PagedResponse<EventResponse> response = advancedEventSearchService.searchEvents(request);
        
        // Log feature usage
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.ADVANCED_SEARCH, "user");
        
        return ResponseEntity.ok(new ApiResponse<>("Events found successfully", "200", response));
    }

    @GetMapping("/recommendations")
    @RequiresFeature("search.advanced-filters")
    @Operation(
        summary = "Get Event Recommendations",
        description = "Get personalized event recommendations based on user preferences and past behavior",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event recommendations retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event recommendations retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "title": "Recommended Event 1",
                                        "description": "This event matches your interests",
                                        "startDate": "2024-12-25T18:00:00",
                                        "endDate": "2024-12-25T22:00:00",
                                        "location": "IIT Campus",
                                        "eventType": "MEETING",
                                        "active": true,
                                        "isOnline": false,
                                        "meetingLink": null,
                                        "maxParticipants": 100,
                                        "currentParticipants": 45,
                                        "organizerName": "John Doe",
                                        "organizerEmail": "john@example.com",
                                        "createdByUsername": "johndoe",
                                        "privacy": "PUBLIC",
                                        "inviteMessage": "Join us for this event",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventRecommendations(
            @Parameter(description = "Username for personalized recommendations") @RequestParam(defaultValue = "user") String username) {
        
        List<EventResponse> response = advancedEventSearchService.getEventRecommendations(username);
        
        return ResponseEntity.ok(new ApiResponse<>("Event recommendations retrieved successfully", "200", response));
    }

    @GetMapping("/trending")
    @Operation(
        summary = "Get Trending Events",
        description = "Get trending events based on engagement metrics and popularity"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Trending events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Trending events retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "title": "Trending Event 1",
                                        "description": "This is a trending event",
                                        "startDate": "2024-12-25T18:00:00",
                                        "endDate": "2024-12-25T22:00:00",
                                        "location": "IIT Campus",
                                        "eventType": "MEETING",
                                        "active": true,
                                        "isOnline": false,
                                        "meetingLink": null,
                                        "maxParticipants": 100,
                                        "currentParticipants": 85,
                                        "organizerName": "John Doe",
                                        "organizerEmail": "john@example.com",
                                        "createdByUsername": "johndoe",
                                        "privacy": "PUBLIC",
                                        "inviteMessage": "Join us for this trending event",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
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
    @Operation(summary = "Get similar events", description = "Get events similar to the specified event")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getSimilarEvents(
            @PathVariable Long eventId,
            @Parameter(description = "Number of events to return") @RequestParam(defaultValue = "10") int limit) {
        
        List<EventResponse> response = advancedEventSearchService.getSimilarEvents(eventId, limit);
        
        return ResponseEntity.ok(new ApiResponse<>("Similar events retrieved successfully", "200", response));
    }
} 