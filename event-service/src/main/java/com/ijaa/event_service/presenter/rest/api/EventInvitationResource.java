package com.ijaa.event_service.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.EventInvitationRequest;
import com.ijaa.event_service.domain.response.EventInvitationResponse;
import com.ijaa.event_service.common.service.BaseService;
import com.ijaa.event_service.service.EventInvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/v1/user/events/invitations")
@Slf4j
@Tag(name = "Event Invitations", description = "APIs for event invitation management")
public class EventInvitationResource extends BaseService {

    private final EventInvitationService eventInvitationService;

    public EventInvitationResource(EventInvitationService eventInvitationService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventInvitationService = eventInvitationService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Send Event Invitations",
        description = "Send invitations to users for an event (only event creator can send invitations)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Invitation details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventInvitationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Send to Multiple Users",
                        summary = "Send invitations to multiple users with personal message",
                        value = """
                            {
                                "eventId": 1,
                                "usernames": ["john.doe", "jane.smith", "bob.wilson"],
                                "personalMessage": "You're invited to our annual alumni meet! Please join us."
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Send to Single User",
                        summary = "Send invitation to a single user",
                        value = """
                            {
                                "eventId": 1,
                                "usernames": ["john.doe"],
                                "personalMessage": "Hope you can make it to the event!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Simple Invitation",
                        summary = "Send invitation without personal message",
                        value = """
                            {
                                "eventId": 1,
                                "usernames": ["john.doe", "jane.smith"]
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
            description = "Invitations sent successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventInvitationResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request or event not found"
        )
    })
    public ResponseEntity<ApiResponse<List<EventInvitationResponse>>> sendInvitations(
            @Valid @RequestBody EventInvitationRequest request) {
        
        String invitedByUsername = getCurrentUsername();
        List<EventInvitationResponse> responses = eventInvitationService.sendInvitations(request, invitedByUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Invitations sent successfully", "200", responses));
    }

    @PostMapping("/{eventId}/accept")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Accept Event Invitation",
        description = "Accept an invitation to an event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<String>> acceptInvitation(@PathVariable Long eventId) {
        String invitedUsername = getCurrentUsername();
        eventInvitationService.acceptInvitation(eventId, invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Invitation accepted successfully", "200", "Invitation accepted"));
    }

    @PostMapping("/{eventId}/decline")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Decline Event Invitation",
        description = "Decline an invitation to an event",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<String>> declineInvitation(@PathVariable Long eventId) {
        String invitedUsername = getCurrentUsername();
        eventInvitationService.declineInvitation(eventId, invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Invitation declined successfully", "200", "Invitation declined"));
    }

    @PostMapping("/{eventId}/mark-read")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Mark Invitation as Read",
        description = "Mark an invitation as read",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<String>> markInvitationAsRead(@PathVariable Long eventId) {
        String invitedUsername = getCurrentUsername();
        eventInvitationService.markInvitationAsRead(eventId, invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Invitation marked as read", "200", "Invitation marked as read"));
    }

    @GetMapping("/my-invitations")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get My Invitations",
        description = "Get all invitations for the current user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<List<EventInvitationResponse>>> getMyInvitations() {
        String invitedUsername = getCurrentUsername();
        List<EventInvitationResponse> invitations = eventInvitationService.getUserInvitations(invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Invitations retrieved successfully", "200", invitations));
    }

    @GetMapping("/my-invitations/unread")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get My Unread Invitations",
        description = "Get unread invitations for the current user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<List<EventInvitationResponse>>> getMyUnreadInvitations() {
        String invitedUsername = getCurrentUsername();
        List<EventInvitationResponse> invitations = eventInvitationService.getUnreadInvitations(invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Unread invitations retrieved successfully", "200", invitations));
    }

    @GetMapping("/my-invitations/unresponded")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get My Unresponded Invitations",
        description = "Get unresponded invitations for the current user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<List<EventInvitationResponse>>> getMyUnrespondedInvitations() {
        String invitedUsername = getCurrentUsername();
        List<EventInvitationResponse> invitations = eventInvitationService.getUnrespondedInvitations(invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Unresponded invitations retrieved successfully", "200", invitations));
    }

    @GetMapping("/{eventId}/invitations")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Event Invitations",
        description = "Get all invitations for an event (only event creator can view)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<List<EventInvitationResponse>>> getEventInvitations(@PathVariable Long eventId) {
        List<EventInvitationResponse> invitations = eventInvitationService.getEventInvitations(eventId);
        
        return ResponseEntity.ok(new ApiResponse<>("Event invitations retrieved successfully", "200", invitations));
    }

    @GetMapping("/sent-by-me")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Invitations Sent by Me",
        description = "Get all invitations sent by the current user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<List<EventInvitationResponse>>> getInvitationsSentByMe() {
        String invitedByUsername = getCurrentUsername();
        List<EventInvitationResponse> invitations = eventInvitationService.getInvitationsSentByUser(invitedByUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Sent invitations retrieved successfully", "200", invitations));
    }

    @GetMapping("/counts")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Invitation Counts",
        description = "Get counts of unread and unresponded invitations for the current user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<Object>> getInvitationCounts() {
        String invitedUsername = getCurrentUsername();
        Long unreadCount = eventInvitationService.countUnreadInvitations(invitedUsername);
        Long unrespondedCount = eventInvitationService.countUnrespondedInvitations(invitedUsername);
        
        return ResponseEntity.ok(new ApiResponse<>("Invitation counts retrieved successfully", "200", 
            new InvitationCountsResponse(unreadCount, unrespondedCount)));
    }
    
    private static class InvitationCountsResponse {
        public final Long unreadCount;
        public final Long unrespondedCount;
        
                public InvitationCountsResponse(Long unreadCount, Long unrespondedCount) {
            this.unreadCount = unreadCount;
            this.unrespondedCount = unrespondedCount;
        }
    }
} 