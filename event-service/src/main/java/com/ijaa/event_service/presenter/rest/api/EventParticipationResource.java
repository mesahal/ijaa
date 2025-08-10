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
                schema = @Schema(implementation = EventParticipationResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request or event not found"
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
        description = "Update your RSVP status for an event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
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
        description = "Cancel your RSVP for an event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
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
        description = "Get your participation status for an event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
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
        description = "Get all participants for an event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
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
        description = "Get all events you are participating in",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<List<EventParticipationResponse>>> getMyParticipations(
            HttpServletRequest httpRequest) {
        
        String username = getCurrentUsername();
        List<EventParticipationResponse> participations = eventParticipationService.getUserParticipations(username);
        
        return ResponseEntity.ok(new ApiResponse<>("Participations retrieved successfully", "200", participations));
    }
} 