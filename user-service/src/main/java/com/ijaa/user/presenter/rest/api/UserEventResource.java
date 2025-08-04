package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.request.EventRequest;
import com.ijaa.user.domain.response.EventResponse;
import com.ijaa.user.service.BaseService;
import com.ijaa.user.service.EventService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/events")
@Slf4j
@Tag(name = "User Event Management", description = "APIs for users to manage their own events")
public class UserEventResource extends BaseService {

    private final EventService eventService;

    public UserEventResource(EventService eventService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventService = eventService;
    }

    @GetMapping("/my-events")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get User's Events",
        description = "Retrieve all events created by the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Forbidden",
                        value = """
                            {
                                "message": "Access denied",
                                "code": "403",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventResponse>>> getUserEvents() {
        String username = getCurrentUsername();
        List<EventResponse> events = eventService.getEventsByUser(username);
        return ResponseEntity.ok(new ApiResponse<>("User events retrieved successfully", "200", events));
    }

    @GetMapping("/my-events/active")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get User's Active Events",
        description = "Retrieve all active events created by the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User active events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
    public ResponseEntity<ApiResponse<List<EventResponse>>> getUserActiveEvents() {
        String username = getCurrentUsername();
        List<EventResponse> events = eventService.getActiveEventsByUser(username);
        return ResponseEntity.ok(new ApiResponse<>("User active events retrieved successfully", "200", events));
    }

    @GetMapping("/all-events")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get All Active Events",
        description = "Retrieve all active events created by all users (public events)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "All active events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Event by ID",
        description = "Retrieve a specific event by ID (public access)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get User's Event by ID",
        description = "Retrieve a specific event created by the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User event retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
        EventResponse event = eventService.getEventByIdForUser(eventId, username);
        return ResponseEntity.ok(new ApiResponse<>("User event retrieved successfully", "200", event));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Create Event",
        description = "Create a new event for the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Event created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
        EventResponse event = eventService.createEventForUser(eventRequest, username);
        return ResponseEntity.ok(new ApiResponse<>("Event created successfully", "201", event));
    }

    @PutMapping("/my-events/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Update User's Event",
        description = "Update an event created by the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
        EventResponse event = eventService.updateEventForUser(eventId, eventRequest, username);
        return ResponseEntity.ok(new ApiResponse<>("Event updated successfully", "200", event));
    }

    @DeleteMapping("/my-events/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Delete User's Event",
        description = "Delete an event created by the authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
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
        eventService.deleteEventForUser(eventId, username);
        return ResponseEntity.ok(new ApiResponse<>("Event deleted successfully", "200", null));
    }
} 