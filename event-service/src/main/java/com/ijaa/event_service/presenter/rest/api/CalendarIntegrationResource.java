package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.common.annotation.RequiresFeature;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.CalendarIntegrationRequest;
import com.ijaa.event_service.domain.response.CalendarIntegrationResponse;
import com.ijaa.event_service.service.CalendarIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar-integrations")
@Slf4j
@Tag(name = "Calendar Integration", description = "Calendar integration and external calendar sync APIs")
public class CalendarIntegrationResource {

    private final CalendarIntegrationService calendarIntegrationService;

    public CalendarIntegrationResource(CalendarIntegrationService calendarIntegrationService) {
        this.calendarIntegrationService = calendarIntegrationService;
    }

    @GetMapping("/user")
    @Operation(summary = "Get user's calendar integrations", description = "Retrieve all calendar integrations for the current user")
    public ResponseEntity<ApiResponse<List<CalendarIntegrationResponse>>> getUserCalendarIntegrations(Authentication authentication) {
        String username = authentication.getName();
        List<CalendarIntegrationResponse> integrations = calendarIntegrationService.getUserCalendarIntegrations(username);
        ApiResponse<List<CalendarIntegrationResponse>> response = new ApiResponse<>();
        response.setMessage("User's calendar integrations retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(integrations);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/active")
    @Operation(summary = "Get user's active calendar integrations", description = "Retrieve active calendar integrations for the current user")
    public ResponseEntity<ApiResponse<List<CalendarIntegrationResponse>>> getActiveCalendarIntegrations(Authentication authentication) {
        String username = authentication.getName();
        List<CalendarIntegrationResponse> integrations = calendarIntegrationService.getActiveCalendarIntegrations(username);
        ApiResponse<List<CalendarIntegrationResponse>> response = new ApiResponse<>();
        response.setMessage("User's active calendar integrations retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(integrations);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{integrationId}")
    @Operation(summary = "Get calendar integration by ID", description = "Retrieve a specific calendar integration by ID")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> getCalendarIntegrationById(@PathVariable Long integrationId) {
        CalendarIntegrationResponse integration = calendarIntegrationService.getCalendarIntegrationById(integrationId);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Calendar integration retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
        summary = "Create Calendar Integration",
        description = "Create a new calendar integration with external calendar services (Google Calendar, Outlook, etc.)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Calendar integration details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CalendarIntegrationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Google Calendar Integration",
                        summary = "Create Google Calendar integration",
                        value = """
                            {
                                "calendarType": "GOOGLE_CALENDAR",
                                "calendarName": "My Google Calendar",
                                "accessToken": "ya29.a0AfH6SMC...",
                                "refreshToken": "1//04dX...",
                                "calendarId": "primary",
                                "syncEvents": true,
                                "syncInvitations": true,
                                "autoSync": true
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Outlook Calendar Integration",
                        summary = "Create Outlook Calendar integration",
                        value = """
                            {
                                "calendarType": "OUTLOOK_CALENDAR",
                                "calendarName": "My Outlook Calendar",
                                "accessToken": "EwBwA8l6BAAU...",
                                "refreshToken": "M.R3_BAY...",
                                "calendarId": "default",
                                "syncEvents": true,
                                "syncInvitations": false,
                                "autoSync": true
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
            description = "Calendar integration created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Calendar integration created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "calendarType": "GOOGLE_CALENDAR",
                                    "calendarName": "My Google Calendar",
                                    "calendarId": "primary",
                                    "syncEvents": true,
                                    "syncInvitations": true,
                                    "autoSync": true,
                                    "status": "ACTIVE",
                                    "lastSyncTime": "2024-12-01T10:00:00",
                                    "createdBy": "john.doe",
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
            description = "Invalid integration data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid calendar integration data provided",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Calendar integration already exists",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Conflict",
                        value = """
                            {
                                "message": "Calendar integration already exists for this user and calendar type",
                                "code": "409",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> createCalendarIntegration(
            @Valid @RequestBody CalendarIntegrationRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        CalendarIntegrationResponse integration = calendarIntegrationService.createCalendarIntegration(request, username);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Calendar integration created successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{integrationId}")
    @Operation(summary = "Update calendar integration", description = "Update an existing calendar integration")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> updateCalendarIntegration(
            @PathVariable Long integrationId,
            @Valid @RequestBody CalendarIntegrationRequest request) {
        CalendarIntegrationResponse integration = calendarIntegrationService.updateCalendarIntegration(integrationId, request);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Calendar integration updated successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{integrationId}")
    @Operation(summary = "Delete calendar integration", description = "Delete a calendar integration")
    public ResponseEntity<ApiResponse<Void>> deleteCalendarIntegration(@PathVariable Long integrationId) {
        calendarIntegrationService.deleteCalendarIntegration(integrationId);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Calendar integration deleted successfully");
        response.setCode("SUCCESS");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/activate")
    @Operation(summary = "Activate calendar integration", description = "Activate a calendar integration")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> activateCalendarIntegration(@PathVariable Long integrationId) {
        CalendarIntegrationResponse integration = calendarIntegrationService.activateCalendarIntegration(integrationId);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Calendar integration activated successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/deactivate")
    @Operation(summary = "Deactivate calendar integration", description = "Deactivate a calendar integration")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> deactivateCalendarIntegration(@PathVariable Long integrationId) {
        CalendarIntegrationResponse integration = calendarIntegrationService.deactivateCalendarIntegration(integrationId);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Calendar integration deactivated successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/sync-events")
    @Operation(summary = "Sync events to calendar", description = "Sync specific events to external calendar")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> syncEventsToCalendar(
            @PathVariable Long integrationId,
            @RequestParam List<Long> eventIds) {
        CalendarIntegrationResponse integration = calendarIntegrationService.syncEventsToCalendar(integrationId, eventIds);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Events synced successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/sync-invitations")
    @Operation(summary = "Sync invitations to calendar", description = "Sync invitations to external calendar")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> syncInvitationsToCalendar(
            @PathVariable Long integrationId,
            @RequestParam List<Long> invitationIds) {
        CalendarIntegrationResponse integration = calendarIntegrationService.syncInvitationsToCalendar(integrationId, invitationIds);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Invitations synced successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/sync-all")
    @Operation(summary = "Sync all user events", description = "Sync all user events to external calendar")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> syncAllUserEvents(
            @PathVariable Long integrationId,
            Authentication authentication) {
        String username = authentication.getName();
        CalendarIntegrationResponse integration = calendarIntegrationService.syncAllUserEvents(integrationId, username);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("All user events synced successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/refresh-tokens")
    @Operation(summary = "Refresh OAuth tokens", description = "Refresh OAuth tokens for calendar integration")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> refreshTokens(@PathVariable Long integrationId) {
        CalendarIntegrationResponse integration = calendarIntegrationService.refreshTokens(integrationId);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Tokens refreshed successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/test-connection")
    @Operation(summary = "Test calendar connection", description = "Test the connection to external calendar")
    public ResponseEntity<ApiResponse<Boolean>> testCalendarConnection(@PathVariable Long integrationId) {
        boolean isConnected = calendarIntegrationService.testCalendarConnection(integrationId);
        ApiResponse<Boolean> response = new ApiResponse<>();
        response.setMessage("Calendar connection test completed");
        response.setCode("SUCCESS");
        response.setData(isConnected);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{integrationId}/sync-status")
    @Operation(summary = "Get sync status", description = "Get the current sync status of calendar integration")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> getSyncStatus(@PathVariable Long integrationId) {
        CalendarIntegrationResponse integration = calendarIntegrationService.getSyncStatus(integrationId);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Sync status retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/failed-syncs")
    @Operation(summary = "Get failed syncs", description = "Get failed syncs for the current user")
    public ResponseEntity<ApiResponse<List<CalendarIntegrationResponse>>> getFailedSyncs(Authentication authentication) {
        String username = authentication.getName();
        List<CalendarIntegrationResponse> failedSyncs = calendarIntegrationService.getFailedSyncs(username);
        ApiResponse<List<CalendarIntegrationResponse>> response = new ApiResponse<>();
        response.setMessage("Failed syncs retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(failedSyncs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{integrationId}/retry-sync")
    @Operation(summary = "Retry failed sync", description = "Retry a failed sync operation")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> retryFailedSync(@PathVariable Long integrationId) {
        CalendarIntegrationResponse integration = calendarIntegrationService.retryFailedSync(integrationId);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("Failed sync retried successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supported-types")
    @Operation(summary = "Get supported calendar types", description = "Get list of supported calendar types")
    public ResponseEntity<ApiResponse<List<String>>> getSupportedCalendarTypes() {
        List<String> calendarTypes = calendarIntegrationService.getSupportedCalendarTypes();
        ApiResponse<List<String>> response = new ApiResponse<>();
        response.setMessage("Supported calendar types retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(calendarTypes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth-url")
    @Operation(summary = "Get OAuth URL", description = "Get OAuth URL for calendar type")
    public ResponseEntity<ApiResponse<String>> getOAuthUrl(
            @RequestParam String calendarType,
            @RequestParam String redirectUri) {
        String oauthUrl = calendarIntegrationService.getOAuthUrl(calendarType, redirectUri);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("OAuth URL generated successfully");
        response.setCode("SUCCESS");
        response.setData(oauthUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/oauth/callback")
    @Operation(summary = "Handle OAuth callback", description = "Handle OAuth callback from external calendar")
    public ResponseEntity<ApiResponse<CalendarIntegrationResponse>> handleOAuthCallback(
            @RequestParam String code,
            @RequestParam String state,
            Authentication authentication) {
        String username = authentication.getName();
        CalendarIntegrationResponse integration = calendarIntegrationService.handleOAuthCallback(code, state, username);
        ApiResponse<CalendarIntegrationResponse> response = new ApiResponse<>();
        response.setMessage("OAuth callback handled successfully");
        response.setCode("SUCCESS");
        response.setData(integration);
        return ResponseEntity.ok(response);
    }
} 