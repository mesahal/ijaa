package com.ijaa.event.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event.common.annotation.RequiresFeature;
import com.ijaa.event.common.annotation.RequiresRole;
import com.ijaa.event.common.utils.AppUtils;
import com.ijaa.event.common.utils.FeatureFlagUtils;
import com.ijaa.event.domain.common.ApiResponse;
import com.ijaa.event.domain.request.EventRequest;
import com.ijaa.event.domain.request.EventSearchRequest;
import com.ijaa.event.domain.response.EventResponse;
import com.ijaa.event.common.service.BaseService;
import com.ijaa.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@Slf4j
@Tag(name = "User Event Management")
public class UserEventResource extends BaseService {

    private final EventService eventService;
    private final FeatureFlagUtils featureFlagUtils;

    public UserEventResource(EventService eventService, ObjectMapper objectMapper, FeatureFlagUtils featureFlagUtils) {
        super(objectMapper);
        this.eventService = eventService;
        this.featureFlagUtils = featureFlagUtils;
    }

    @GetMapping("/my-events")
    @RequiresRole("USER")
    @RequiresFeature("events")
    @Operation(
        summary = "Get User's Events",
        description = "Retrieve all events created by the authenticated user (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "User events retrieved successfully",
                                "code": "200",
                                "data": [
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
                                        "maxParticipants": 100,
                                        "currentParticipants": 0,
                                        "organizerName": "John Doe",
                                        "organizerEmail": "john@example.com",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    }
                                ]
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
    public ResponseEntity<ApiResponse<List<EventResponse>>> getMyEvents() {
        String username = getCurrentUsername();
        List<EventResponse> events = eventService.getEventsByUser(username);
        return ResponseEntity.ok(new ApiResponse<>("User events retrieved successfully", "200", events));
    }

    @GetMapping("/my-events/active")
    @RequiresRole("USER")
    @RequiresFeature("events")
    @Operation(
        summary = "Get User's Active Events",
        description = "Retrieve all active events created by the authenticated user (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User active events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "User active events retrieved successfully",
                                "code": "200",
                                "data": [
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
                                        "maxParticipants": 100,
                                        "currentParticipants": 0,
                                        "organizerName": "John Doe",
                                        "organizerEmail": "john@example.com",
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
    public ResponseEntity<ApiResponse<List<EventResponse>>> getMyActiveEvents() {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        List<EventResponse> events = eventService.getActiveEventsByUser(username);
        return ResponseEntity.ok(new ApiResponse<>("User active events retrieved successfully", "200", events));
    }

    @GetMapping("/all-events")
    @RequiresRole("USER")
    @RequiresFeature("events")
    @Operation(
        summary = "Get All Events",
        description = "Retrieve all events (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "All active events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "All active events retrieved successfully",
                                "code": "200",
                                "data": [
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
                                        "maxParticipants": 100,
                                        "currentParticipants": 0,
                                        "organizerName": "John Doe",
                                        "organizerEmail": "john@example.com",
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
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllActiveEvents() {
        List<EventResponse> events = eventService.getActiveEvents();
        return ResponseEntity.ok(new ApiResponse<>("All active events retrieved successfully", "200", events));
    }

    @GetMapping("/all-events/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events")
    @Operation(
        summary = "Get Event by ID",
        description = "Retrieve a specific event by ID (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event retrieved successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "title": "Alumni Meet 2024",
                                    "description": "Annual alumni gathering",
                                    "startDate": "2024-12-25T18:00:00",
                                    "endDate": "2024-12-25T22:00:00",
                                    "location": "IIT Campus",
                                    "eventType": "MEETING",
                                    "active": true,
                                    "isOnline": false,
                                    "maxParticipants": 100,
                                    "currentParticipants": 0,
                                    "organizerName": "John Doe",
                                    "organizerEmail": "john@example.com",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Long eventId) {
        EventResponse event = eventService.getEventById(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Event retrieved successfully", "200", event));
    }

    @GetMapping("/my-events/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events")
    @Operation(
        summary = "Get My Event by ID",
        description = "Retrieve a specific event created by the authenticated user (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User event retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "User event retrieved successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "title": "Alumni Meet 2024",
                                    "description": "Annual alumni gathering",
                                    "startDate": "2024-12-25T18:00:00",
                                    "endDate": "2024-12-25T22:00:00",
                                    "location": "IIT Campus",
                                    "eventType": "MEETING",
                                    "active": true,
                                    "isOnline": false,
                                    "maxParticipants": 100,
                                    "currentParticipants": 0,
                                    "organizerName": "John Doe",
                                    "organizerEmail": "john@example.com",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found or you don't have permission to view it",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventResponse>> getUserEventById(@PathVariable Long eventId) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        EventResponse event = eventService.getEventByIdForUser(eventId, username);
        return ResponseEntity.ok(new ApiResponse<>("User event retrieved successfully", "200", event));
    }

    @PostMapping("/create")
    @RequiresRole("USER")
    @RequiresFeature("events.creation")
    @Operation(
        summary = "Create Event",
        description = "Create a new event for the authenticated user (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Event creation details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Event Creation",
                        summary = "Complete event creation with all fields",
                        value = """
                            {
                                "title": "Alumni Meet 2024",
                                "description": "Annual alumni gathering and networking event",
                                "startDate": "2024-12-25T18:00:00",
                                "endDate": "2024-12-25T22:00:00",
                                "location": "IIT Campus, Main Auditorium",
                                "eventType": "MEETING",
                                "isOnline": false,
                                "meetingLink": null,
                                "maxParticipants": 100,
                                "organizerName": "John Doe",
                                "organizerEmail": "john.doe@example.com",
                                "privacy": "PUBLIC",
                                "inviteMessage": "Join us for the annual alumni meet!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Online Event",
                        summary = "Online event with meeting link",
                        value = """
                            {
                                "title": "Virtual Alumni Meet",
                                "description": "Online alumni networking session",
                                "startDate": "2024-12-20T14:00:00",
                                "endDate": "2024-12-20T16:00:00",
                                "location": "Virtual",
                                "eventType": "WEBINAR",
                                "isOnline": true,
                                "meetingLink": "https://meet.google.com/abc-defg-hij",
                                "maxParticipants": 50,
                                "organizerName": "Jane Smith",
                                "organizerEmail": "jane.smith@example.com",
                                "privacy": "INVITE_ONLY",
                                "inviteMessage": "Join our virtual alumni meet!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Minimal Event",
                        summary = "Event with minimal required fields",
                        value = """
                            {
                                "title": "Quick Meet",
                                "description": "Quick alumni meet",
                                "startDate": "2024-12-30T19:00:00",
                                "endDate": "2024-12-30T21:00:00",
                                "maxParticipants": 20,
                                "organizerName": "Bob Wilson",
                                "organizerEmail": "bob.wilson@example.com"
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
            description = "Event created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "title": "Alumni Meet 2024",
                                    "description": "Annual alumni gathering",
                                    "startDate": "2024-12-25T18:00:00",
                                    "endDate": "2024-12-25T22:00:00",
                                    "location": "IIT Campus",
                                    "eventType": "MEETING",
                                    "active": true,
                                    "isOnline": false,
                                    "maxParticipants": 100,
                                    "currentParticipants": 0,
                                    "organizerName": "John Doe",
                                    "organizerEmail": "john@example.com",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid event request",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Start date cannot be after end date",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        EventResponse event = eventService.createEventForUser(eventRequest, username);
        return ResponseEntity.ok(new ApiResponse<>("Event created successfully", "201", event));
    }

    @PutMapping("/my-events/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events.update")
    @Operation(
        summary = "Update User's Event",
        description = "Update an event created by the authenticated user (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated event details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Update Event Details",
                        summary = "Update event with new information",
                        value = """
                            {
                                "title": "Updated Alumni Meet 2024",
                                "description": "Updated annual alumni gathering with new agenda",
                                "startDate": "2024-12-25T18:00:00",
                                "endDate": "2024-12-25T22:00:00",
                                "location": "IIT Campus, New Auditorium",
                                "eventType": "MEETING",
                                "isOnline": false,
                                "meetingLink": null,
                                "maxParticipants": 150,
                                "organizerName": "John Doe",
                                "organizerEmail": "john.doe@example.com",
                                "privacy": "PUBLIC",
                                "inviteMessage": "Updated: Join us for the annual alumni meet!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Convert to Online Event",
                        summary = "Convert physical event to online",
                        value = """
                            {
                                "title": "Virtual Alumni Meet 2024",
                                "description": "Annual alumni gathering (now online)",
                                "startDate": "2024-12-25T18:00:00",
                                "endDate": "2024-12-25T22:00:00",
                                "location": "Virtual",
                                "eventType": "WEBINAR",
                                "isOnline": true,
                                "meetingLink": "https://meet.google.com/abc-defg-hij",
                                "maxParticipants": 100,
                                "organizerName": "John Doe",
                                "organizerEmail": "john.doe@example.com",
                                "privacy": "PUBLIC",
                                "inviteMessage": "Join our virtual alumni meet!"
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
            description = "Event updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "title": "Updated Alumni Meet 2024",
                                    "description": "Updated annual alumni gathering",
                                    "startDate": "2024-12-25T18:00:00",
                                    "endDate": "2024-12-25T22:00:00",
                                    "location": "IIT Campus",
                                    "eventType": "MEETING",
                                    "active": true,
                                    "isOnline": false,
                                    "maxParticipants": 100,
                                    "currentParticipants": 0,
                                    "organizerName": "John Doe",
                                    "organizerEmail": "john@example.com",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T11:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found or you don't have permission to update it",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventResponse>> updateUserEvent(
            @PathVariable Long eventId, 
            @Valid @RequestBody EventRequest eventRequest) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        EventResponse event = eventService.updateEventForUser(eventId, eventRequest, username);
        return ResponseEntity.ok(new ApiResponse<>("Event updated successfully", "200", event));
    }

    @DeleteMapping("/my-events/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events.delete")
    @Operation(
        summary = "Delete User's Event",
        description = "Delete an event created by the authenticated user (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event deleted successfully",
                                "code": "200",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found or you don't have permission to delete it",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteUserEvent(@PathVariable Long eventId) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        eventService.deleteEventForUser(eventId, username);
        return ResponseEntity.ok(new ApiResponse<>("Event deleted successfully", "200", null));
    }

    @PostMapping("/search")
    @RequiresRole("USER")
    @RequiresFeature("search")
    @Operation(
        summary = "Search Events",
        description = "Search events with various criteria (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Events found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Events found successfully",
                                "code": "200",
                                "data": [
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
                                        "maxParticipants": 100,
                                        "currentParticipants": 0,
                                        "organizerName": "John Doe",
                                        "organizerEmail": "john@example.com"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid search parameters provided",
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
    public ResponseEntity<ApiResponse<List<EventResponse>>> searchEvents(@RequestBody EventSearchRequest request) {
        List<EventResponse> events = eventService.searchEvents(
            request.getLocation(), 
            request.getEventType(), 
            request.getStartDate(), 
            request.getEndDate(), 
            request.getIsOnline(), 
            request.getOrganizerName(), 
            request.getTitle(), 
            request.getDescription()
        );
        return ResponseEntity.ok(new ApiResponse<>("Events found successfully", "200", events));
    }
} 
