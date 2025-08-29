package com.ijaa.event_service.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event_service.common.annotation.RequiresFeature;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.EventReminderRequest;
import com.ijaa.event_service.domain.response.EventReminderResponse;
import com.ijaa.event_service.service.EventReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

@RestController
@RequestMapping("/api/v1/user/events/reminders")
@Slf4j
@Tag(name = "Event Reminders", description = "APIs for managing event reminders and notifications")
public class EventReminderResource {

    private final EventReminderService eventReminderService;

    public EventReminderResource(EventReminderService eventReminderService) {
        this.eventReminderService = eventReminderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.reminders")
    @Operation(
        summary = "Set Event Reminder",
        description = "Set a reminder for an event with customizable timing and notification preferences (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Event reminder details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventReminderRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Standard Reminder",
                        summary = "Set a standard reminder for an event",
                        value = """
                            {
                                "eventId": 1,
                                "reminderTime": "2024-12-25T17:00:00",
                                "reminderType": "EMAIL",
                                "message": "Don't forget about the alumni meet tomorrow!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Multiple Reminders",
                        summary = "Set multiple reminders for an event",
                        value = """
                            {
                                "eventId": 1,
                                "reminderTime": "2024-12-25T17:00:00",
                                "reminderType": "BOTH",
                                "message": "Alumni meet reminder",
                                "additionalReminders": [
                                    {
                                        "reminderTime": "2024-12-25T09:00:00",
                                        "reminderType": "SMS",
                                        "message": "Alumni meet today at 6 PM"
                                    }
                                ]
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
            description = "Reminder set successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Reminder set successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "eventId": 1,
                                    "eventTitle": "Alumni Meet 2024",
                                    "reminderTime": "2024-12-25T17:00:00",
                                    "reminderType": "EMAIL",
                                    "message": "Don't forget about the alumni meet tomorrow!",
                                    "status": "ACTIVE",
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
            description = "Invalid reminder data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid reminder data provided",
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
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Reminder already exists",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Conflict",
                        value = """
                            {
                                "message": "Reminder already exists for this event",
                                "code": "409",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventReminderResponse>> setReminder(@Valid @RequestBody EventReminderRequest request) {
        // TODO: Extract username from security context
        String username = "current-user"; // Placeholder - should be extracted from JWT token
        EventReminderResponse reminder = eventReminderService.setReminder(request, username);
        return ResponseEntity.status(201).body(new ApiResponse<>("Reminder set successfully", "201", reminder));
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Event Reminders",
        description = "Get all reminders for a specific event (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event reminders retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getEventReminders(@PathVariable Long eventId) {
        List<EventReminderResponse> reminders = eventReminderService.getEventReminders(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Event reminders retrieved successfully", "200", reminders));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get User's Reminders",
        description = "Get all reminders for the authenticated user (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User reminders retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getUserReminders() {
        // TODO: Extract username from security context
        String username = "current-user"; // Placeholder - should be extracted from JWT token
        List<EventReminderResponse> reminders = eventReminderService.getUserReminders(username);
        return ResponseEntity.ok(new ApiResponse<>("User reminders retrieved successfully", "200", reminders));
    }

    @GetMapping("/{reminderId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Reminder by ID",
        description = "Get a specific reminder by ID (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reminder retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Reminder not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Reminder not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventReminderResponse>> getReminderById(@PathVariable Long reminderId) {
        EventReminderResponse reminder = eventReminderService.getReminder(reminderId);
        return ResponseEntity.ok(new ApiResponse<>("Reminder retrieved successfully", "200", reminder));
    }

    @PutMapping("/{reminderId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Update Reminder",
        description = "Update an existing reminder (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated reminder details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventReminderRequest.class)
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reminder updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Reminder not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Reminder not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventReminderResponse>> updateReminder(
            @PathVariable Long reminderId,
            @Valid @RequestBody EventReminderRequest request) {
        // TODO: Extract username from security context
        String username = "current-user"; // Placeholder - should be extracted from JWT token
        EventReminderResponse reminder = eventReminderService.updateReminder(reminderId, request, username);
        return ResponseEntity.ok(new ApiResponse<>("Reminder updated successfully", "200", reminder));
    }

    @DeleteMapping("/{reminderId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Delete Reminder",
        description = "Delete a reminder (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reminder deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Reminder not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Reminder not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteReminder(@PathVariable Long reminderId) {
        // TODO: Extract username from security context
        String username = "current-user"; // Placeholder - should be extracted from JWT token
        eventReminderService.deleteReminder(reminderId, username);
        return ResponseEntity.ok(new ApiResponse<>("Reminder deleted successfully", "200", null));
    }

    @GetMapping("/user/sent")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Sent Reminders",
        description = "Get all reminders sent by the authenticated user (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sent reminders retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getSentReminders() {
        // TODO: Extract username from security context
        String username = "current-user"; // Placeholder - should be extracted from JWT token
        var reminders = eventReminderService.getSentReminders(username, 0, 100); // Default pagination
        return ResponseEntity.ok(new ApiResponse<>("Sent reminders retrieved successfully", "200", reminders.getContent()));
    }

    @GetMapping("/type/{reminderType}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Reminders by Type",
        description = "Get reminders filtered by type (EMAIL, SMS, BOTH) (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reminders by type retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getRemindersByType(@PathVariable String reminderType) {
        List<EventReminderResponse> reminders = eventReminderService.getRemindersByType(reminderType);
        return ResponseEntity.ok(new ApiResponse<>("Reminders by type retrieved successfully", "200", reminders));
    }

    @GetMapping("/channel/{channel}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get Reminders by Channel",
        description = "Get reminders filtered by notification channel (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reminders by channel retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getRemindersByChannel(@PathVariable String channel) {
        List<EventReminderResponse> reminders = eventReminderService.getRemindersByChannel(channel);
        return ResponseEntity.ok(new ApiResponse<>("Reminders by channel retrieved successfully", "200", reminders));
    }
} 