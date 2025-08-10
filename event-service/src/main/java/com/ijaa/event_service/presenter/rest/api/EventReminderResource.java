package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.EventReminderRequest;
import com.ijaa.event_service.domain.response.EventReminderResponse;
import com.ijaa.event_service.service.EventReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/events/reminders")
@RequiredArgsConstructor
@Tag(name = "Event Reminders", description = "Event reminder management APIs")
public class EventReminderResource {

    private final EventReminderService eventReminderService;

    @PostMapping
    @Operation(summary = "Set a reminder", description = "Set a reminder for an event")
    public ResponseEntity<ApiResponse<EventReminderResponse>> setReminder(
            @Valid @RequestBody EventReminderRequest request,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventReminderResponse response = eventReminderService.setReminder(request, username);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Reminder set successfully", "201", response));
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get event reminders", description = "Get all reminders for an event")
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getEventReminders(@PathVariable Long eventId) {
        
        List<EventReminderResponse> response = eventReminderService.getEventReminders(eventId);
        
        return ResponseEntity.ok(new ApiResponse<>("Event reminders retrieved successfully", "200", response));
    }

    @GetMapping("/user")
    @Operation(summary = "Get user's reminders", description = "Get all reminders for the current user")
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getUserReminders(
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        List<EventReminderResponse> response = eventReminderService.getUserReminders(username);
        
        return ResponseEntity.ok(new ApiResponse<>("User reminders retrieved successfully", "200", response));
    }

    @GetMapping("/{reminderId}")
    @Operation(summary = "Get a specific reminder", description = "Get a specific reminder by ID")
    public ResponseEntity<ApiResponse<EventReminderResponse>> getReminder(@PathVariable Long reminderId) {
        
        EventReminderResponse response = eventReminderService.getReminder(reminderId);
        
        return ResponseEntity.ok(new ApiResponse<>("Reminder retrieved successfully", "200", response));
    }

    @PutMapping("/{reminderId}")
    @Operation(summary = "Update a reminder", description = "Update a reminder")
    public ResponseEntity<ApiResponse<EventReminderResponse>> updateReminder(
            @PathVariable Long reminderId,
            @Valid @RequestBody EventReminderRequest request,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventReminderResponse response = eventReminderService.updateReminder(reminderId, request, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Reminder updated successfully", "200", response));
    }

    @DeleteMapping("/{reminderId}")
    @Operation(summary = "Delete a reminder", description = "Delete a reminder")
    public ResponseEntity<ApiResponse<Void>> deleteReminder(
            @PathVariable Long reminderId,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        eventReminderService.deleteReminder(reminderId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Reminder deleted successfully", "200", null));
    }

    @GetMapping("/user/sent")
    @Operation(summary = "Get sent reminders", description = "Get paginated sent reminders for the current user")
    public ResponseEntity<ApiResponse<PagedResponse<EventReminderResponse>>> getSentReminders(
            @RequestHeader("X-USER_ID") String userContext,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        String username = extractUsername(userContext);
        PagedResponse<EventReminderResponse> response = eventReminderService.getSentReminders(username, page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("Sent reminders retrieved successfully", "200", response));
    }

    @GetMapping("/type/{reminderType}")
    @Operation(summary = "Get reminders by type", description = "Get reminders by type")
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getRemindersByType(
            @PathVariable String reminderType) {
        
        List<EventReminderResponse> response = eventReminderService.getRemindersByType(reminderType);
        
        return ResponseEntity.ok(new ApiResponse<>("Reminders by type retrieved successfully", "200", response));
    }

    @GetMapping("/channel/{channel}")
    @Operation(summary = "Get reminders by channel", description = "Get reminders by notification channel")
    public ResponseEntity<ApiResponse<List<EventReminderResponse>>> getRemindersByChannel(
            @PathVariable String channel) {
        
        List<EventReminderResponse> response = eventReminderService.getRemindersByChannel(channel);
        
        return ResponseEntity.ok(new ApiResponse<>("Reminders by channel retrieved successfully", "200", response));
    }

    private String extractUsername(String userContext) {
        // This is a simplified implementation
        // In a real app, you'd decode the JWT token or user context
        return userContext; // Assuming userContext contains the username
    }
} 