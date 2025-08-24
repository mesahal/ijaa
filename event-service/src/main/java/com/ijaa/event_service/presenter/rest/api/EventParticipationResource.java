package com.ijaa.event_service.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.EventParticipationRequest;
import com.ijaa.event_service.domain.response.EventParticipationResponse;
import com.ijaa.event_service.common.service.BaseService;
import com.ijaa.event_service.service.EventParticipationService;
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
@RequestMapping("/api/v1/user/events/participation")
@Slf4j
@Tag(name = "Event Participation", description = "APIs for event RSVP and participation management")
public class EventParticipationResource extends BaseService {

    private final EventParticipationService eventParticipationService;

    public EventParticipationResource(EventParticipationService eventParticipationService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventParticipationService = eventParticipationService;
    }

    @PostMapping("/rsvp")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "RSVP to Event",
        description = "RSVP to an event with GOING, MAYBE, or NOT_GOING status",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "RSVP details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventParticipationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Going to Event",
                        summary = "RSVP as going to the event",
                        value = """
                            {
                                "eventId": 1,
                                "status": "GOING",
                                "message": "Looking forward to attending!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Maybe Attending",
                        summary = "RSVP as maybe attending",
                        value = """
                            {
                                "eventId": 1,
                                "status": "MAYBE",
                                "message": "I'll try to make it if my schedule allows"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Not Going",
                        summary = "RSVP as not going",
                        value = """
                            {
                                "eventId": 1,
                                "status": "NOT_GOING",
                                "message": "Sorry, I have a prior commitment"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Simple RSVP",
                        summary = "RSVP without message",
                        value = """
                            {
                                "eventId": 1,
                                "status": "GOING"
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
            description = "RSVP successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "RSVP successful",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "eventId": 1,
                                    "participantUsername": "john.doe",
                                    "status": "GOING",
                                    "message": "Looking forward to attending!",
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
            description = "Invalid request or event not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Event not found",
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
    public ResponseEntity<ApiResponse<EventParticipationResponse>> rsvpToEvent(
            @Valid @RequestBody EventParticipationRequest request,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        EventParticipationResponse response = eventParticipationService.rsvpToEvent(request, username);
        
        return ResponseEntity.ok(new ApiResponse<>("RSVP successful", "200", response));
    }

    @PutMapping("/{eventId}/rsvp")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Update RSVP Status",
        description = "Update your RSVP status for an event (GOING, MAYBE, NOT_GOING)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "RSVP updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "RSVP updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "eventId": 1,
                                    "participantUsername": "john.doe",
                                    "status": "MAYBE",
                                    "message": "I'll try to make it if my schedule allows",
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
            description = "Event or participation not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event or participation not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid status value",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Status",
                        value = """
                            {
                                "message": "Invalid status. Must be GOING, MAYBE, or NOT_GOING",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventParticipationResponse>> updateRsvp(
            @PathVariable Long eventId,
            @RequestParam String status,
            @RequestParam(required = false) String message,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        EventParticipationResponse response = eventParticipationService.updateRsvp(eventId, status, message, username);
        
        return ResponseEntity.ok(new ApiResponse<>("RSVP updated successfully", "200", response));
    }

    @DeleteMapping("/{eventId}/rsvp")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Cancel RSVP",
        description = "Cancel your RSVP for an event (removes participation record)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "RSVP cancelled successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "RSVP cancelled successfully",
                                "code": "200",
                                "data": "Your RSVP has been cancelled"
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event or participation not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event or participation not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> cancelRsvp(
            @PathVariable Long eventId,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        eventParticipationService.cancelRsvp(eventId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("RSVP cancelled successfully", "200", "RSVP cancelled"));
    }

    @GetMapping("/{eventId}/my-participation")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get My Participation",
        description = "Get your participation status for a specific event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Participation status retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Participation status retrieved successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "eventId": 1,
                                    "participantUsername": "john.doe",
                                    "status": "GOING",
                                    "message": "Looking forward to attending!",
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
            description = "Event not found or no participation record",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found or you haven't RSVP'd to this event",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventParticipationResponse>> getMyParticipation(
            @PathVariable Long eventId,
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        EventParticipationResponse response = eventParticipationService.getUserParticipation(eventId, username);
        
        if (response == null) {
            return ResponseEntity.ok(new ApiResponse<>("No participation found", "200", null));
        }
        
        return ResponseEntity.ok(new ApiResponse<>("Participation retrieved successfully", "200", response));
    }

    @GetMapping("/{eventId}/participants")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Event Participants",
        description = "Get all participants for an event (all statuses)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event participants retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event participants retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "eventId": 1,
                                        "participantUsername": "john.doe",
                                        "status": "GOING",
                                        "message": "Looking forward to attending!",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    },
                                    {
                                        "id": 2,
                                        "eventId": 1,
                                        "participantUsername": "jane.smith",
                                        "status": "MAYBE",
                                        "message": "I'll try to make it",
                                        "createdAt": "2024-12-01T11:00:00",
                                        "updatedAt": "2024-12-01T11:00:00"
                                    }
                                ]
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
    public ResponseEntity<ApiResponse<List<EventParticipationResponse>>> getEventParticipants(
            @PathVariable Long eventId) {
        
        List<EventParticipationResponse> participants = eventParticipationService.getEventParticipations(eventId);
        
        return ResponseEntity.ok(new ApiResponse<>("Participants retrieved successfully", "200", participants));
    }

    @GetMapping("/{eventId}/participants/{status}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Participants by Status",
        description = "Get participants for an event by status (GOING, MAYBE, NOT_GOING)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Participants by status retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response - GOING",
                        value = """
                            {
                                "message": "Participants by status retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "eventId": 1,
                                        "participantUsername": "john.doe",
                                        "status": "GOING",
                                        "message": "Looking forward to attending!",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    },
                                    {
                                        "id": 3,
                                        "eventId": 1,
                                        "participantUsername": "mike.johnson",
                                        "status": "GOING",
                                        "message": "Can't wait!",
                                        "createdAt": "2024-12-01T12:00:00",
                                        "updatedAt": "2024-12-01T12:00:00"
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
            description = "Invalid status value",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Status",
                        value = """
                            {
                                "message": "Invalid status. Must be GOING, MAYBE, or NOT_GOING",
                                "code": "400",
                                "data": null
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
    public ResponseEntity<ApiResponse<List<EventParticipationResponse>>> getParticipantsByStatus(
            @PathVariable Long eventId,
            @PathVariable String status) {
        
        List<EventParticipationResponse> participants = eventParticipationService.getEventParticipationsByStatus(eventId, status);
        
        return ResponseEntity.ok(new ApiResponse<>("Participants retrieved successfully", "200", participants));
    }

    @GetMapping("/my-participations")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get My Participations",
        description = "Get all events you are participating in (all your RSVPs)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "My participations retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "My participations retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "eventId": 1,
                                        "participantUsername": "john.doe",
                                        "status": "GOING",
                                        "message": "Looking forward to attending!",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    },
                                    {
                                        "id": 2,
                                        "eventId": 3,
                                        "participantUsername": "john.doe",
                                        "status": "MAYBE",
                                        "message": "I'll try to make it",
                                        "createdAt": "2024-12-02T10:00:00",
                                        "updatedAt": "2024-12-02T10:00:00"
                                    },
                                    {
                                        "id": 3,
                                        "eventId": 5,
                                        "participantUsername": "john.doe",
                                        "status": "NOT_GOING",
                                        "message": "Sorry, I have a prior commitment",
                                        "createdAt": "2024-12-03T10:00:00",
                                        "updatedAt": "2024-12-03T10:00:00"
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
    public ResponseEntity<ApiResponse<List<EventParticipationResponse>>> getMyParticipations(
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        List<EventParticipationResponse> participations = eventParticipationService.getUserParticipations(username);
        
        return ResponseEntity.ok(new ApiResponse<>("Participations retrieved successfully", "200", participations));
    }
} 