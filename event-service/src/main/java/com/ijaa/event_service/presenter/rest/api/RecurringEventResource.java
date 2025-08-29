package com.ijaa.event_service.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event_service.common.annotation.RequiresFeature;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.RecurringEventRequest;
import com.ijaa.event_service.domain.response.RecurringEventResponse;
import com.ijaa.event_service.common.service.BaseService;
import com.ijaa.event_service.service.RecurringEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/events/recurring")
@Slf4j
@Tag(name = "Recurring Events", description = "APIs for managing recurring events")
public class RecurringEventResource extends BaseService {

    private final RecurringEventService recurringEventService;

    public RecurringEventResource(RecurringEventService recurringEventService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.recurringEventService = recurringEventService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get All Recurring Events",
        description = "Retrieve all active recurring events",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recurring events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<RecurringEventResponse>>> getAllRecurringEvents() {
        List<RecurringEventResponse> events = recurringEventService.getActiveRecurringEvents();
        return ResponseEntity.ok(new ApiResponse<>("Recurring events retrieved successfully", "200", events));
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Recurring Event by ID",
        description = "Retrieve a specific recurring event by its ID",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recurring event retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Recurring event not found"
        )
    })
    public ResponseEntity<ApiResponse<RecurringEventResponse>> getRecurringEventById(@PathVariable Long eventId) {
        RecurringEventResponse event = recurringEventService.getRecurringEventById(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Recurring event retrieved successfully", "200", event));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.recurring")
    @Operation(
        summary = "Create Recurring Event",
        description = "Create a recurring event with pattern (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Recurring event creation details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Weekly Recurring Event",
                        summary = "Create a weekly recurring event",
                        value = """
                            {
                                "title": "Weekly Alumni Meet",
                                "description": "Weekly alumni networking session",
                                "startDate": "2024-12-25T18:00:00",
                                "endDate": "2024-12-25T22:00:00",
                                "location": "IIT Campus",
                                "eventType": "MEETING",
                                "isOnline": false,
                                "maxParticipants": 50,
                                "organizerName": "John Doe",
                                "organizerEmail": "john@example.com",
                                "recurrenceType": "WEEKLY",
                                "recurrenceInterval": 1,
                                "endAfterOccurrences": 12
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Monthly Recurring Event",
                        summary = "Create a monthly recurring event",
                        value = """
                            {
                                "title": "Monthly Tech Talk",
                                "description": "Monthly technology discussion",
                                "startDate": "2024-12-30T14:00:00",
                                "endDate": "2024-12-30T16:00:00",
                                "location": "Virtual",
                                "eventType": "WEBINAR",
                                "isOnline": true,
                                "meetingLink": "https://meet.google.com/abc-defg-hij",
                                "maxParticipants": 100,
                                "organizerName": "Tech Group",
                                "organizerEmail": "tech@example.com",
                                "recurrenceType": "MONTHLY",
                                "recurrenceInterval": 1,
                                "endDate": "2025-12-30T16:00:00"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Recurring event created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Recurring event created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "title": "Weekly Alumni Meet",
                                    "description": "Weekly alumni networking session",
                                    "startDate": "2024-12-25T18:00:00",
                                    "endDate": "2024-12-25T22:00:00",
                                    "location": "IIT Campus",
                                    "eventType": "MEETING",
                                    "isOnline": false,
                                    "maxParticipants": 50,
                                    "organizerName": "John Doe",
                                    "organizerEmail": "john@example.com",
                                    "recurrenceType": "WEEKLY",
                                    "recurrenceInterval": 1,
                                    "endAfterOccurrences": 12,
                                    "active": true,
                                    "createdBy": "john.doe",
                                    "createdAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid recurring event data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid recurring event data provided",
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
    public ResponseEntity<ApiResponse<RecurringEventResponse>> createRecurringEvent(
            @Valid @RequestBody RecurringEventRequest request,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        RecurringEventResponse response = recurringEventService.createRecurringEventForUser(request, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Recurring event created successfully", "200", response));
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Update Recurring Event",
        description = "Update an existing recurring event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recurring event updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Recurring event not found"
        )
    })
    public ResponseEntity<ApiResponse<RecurringEventResponse>> updateRecurringEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody RecurringEventRequest request,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        RecurringEventResponse response = recurringEventService.updateRecurringEventForUser(eventId, request, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Recurring event updated successfully", "200", response));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Delete Recurring Event",
        description = "Delete a recurring event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recurring event deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Recurring event not found"
        )
    })
    public ResponseEntity<ApiResponse<String>> deleteRecurringEvent(
            @PathVariable Long eventId,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        recurringEventService.deleteRecurringEventForUser(eventId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Recurring event deleted successfully", "200", "Event deleted"));
    }

    @GetMapping("/my-recurring-events")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get User's Recurring Events",
        description = "Retrieve all recurring events created by the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User's recurring events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<RecurringEventResponse>>> getMyRecurringEvents(HttpServletRequest httpRequest) {
        String username = getCurrentUsername();
        List<RecurringEventResponse> events = recurringEventService.getActiveRecurringEventsByUser(username);
        
        return ResponseEntity.ok(new ApiResponse<>("User's recurring events retrieved successfully", "200", events));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Search Recurring Events",
        description = "Search recurring events with various filters",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<RecurringEventResponse>>> searchRecurringEvents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Boolean isOnline,
            @RequestParam(required = false) String organizerName,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String recurrenceType) {
        
        LocalDateTime startDateTime = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime endDateTime = endDate != null ? LocalDateTime.parse(endDate) : null;
        
        List<RecurringEventResponse> events = recurringEventService.searchRecurringEvents(
            location, eventType, startDateTime, endDateTime, isOnline, organizerName, title, description, recurrenceType);
        
        return ResponseEntity.ok(new ApiResponse<>("Search results retrieved successfully", "200", events));
    }

    @PostMapping("/{eventId}/activate")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Activate Recurring Event",
        description = "Activate a recurring event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recurring event activated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<RecurringEventResponse>> activateRecurringEvent(@PathVariable Long eventId) {
        RecurringEventResponse response = recurringEventService.activateRecurringEvent(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Recurring event activated successfully", "200", response));
    }

    @PostMapping("/{eventId}/deactivate")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Deactivate Recurring Event",
        description = "Deactivate a recurring event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recurring event deactivated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecurringEventResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<RecurringEventResponse>> deactivateRecurringEvent(@PathVariable Long eventId) {
        RecurringEventResponse response = recurringEventService.deactivateRecurringEvent(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Recurring event deactivated successfully", "200", response));
    }
} 