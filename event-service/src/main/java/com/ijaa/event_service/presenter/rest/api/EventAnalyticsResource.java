package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.common.annotation.RequiresFeature;
import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.EventAnalyticsRequest;
import com.ijaa.event_service.domain.response.EventAnalyticsResponse;
import com.ijaa.event_service.service.EventAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/user/events/analytics")
@Slf4j
@Tag(name = "Event Analytics", description = "APIs for event analytics and reporting")
public class EventAnalyticsResource {

    private final EventAnalyticsService eventAnalyticsService;

    public EventAnalyticsResource(EventAnalyticsService eventAnalyticsService) {
        this.eventAnalyticsService = eventAnalyticsService;
    }

    @GetMapping("/{eventId}")
    @Operation(
        summary = "Get Event Analytics",
        description = "Retrieve comprehensive analytics for a specific event including attendance, response rates, and engagement metrics",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event analytics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event analytics retrieved successfully",
                                "code": "200",
                                "data": {
                                    "eventId": 1,
                                    "totalInvitations": 150,
                                    "totalResponses": 120,
                                    "goingCount": 85,
                                    "maybeCount": 25,
                                    "notGoingCount": 10,
                                    "actualAttendees": 78,
                                    "noShows": 7,
                                    "responseRate": 80.0,
                                    "attendanceRate": 91.8,
                                    "engagementScore": 85.5,
                                    "averageResponseTime": "2.5 days",
                                    "topEngagementFactors": ["Location", "Timing", "Content"],
                                    "lastUpdated": "2024-12-01T15:30:00"
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
    public ResponseEntity<ApiResponse<EventAnalyticsResponse>> getEventAnalytics(@PathVariable Long eventId) {
        EventAnalyticsResponse analytics = eventAnalyticsService.getEventAnalytics(eventId);
        ApiResponse<EventAnalyticsResponse> response = new ApiResponse<>();
        response.setMessage("Event analytics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
        summary = "Update Event Analytics",
        description = "Create or update analytics for an event with detailed metrics and tracking data",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Event analytics data to create or update",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventAnalyticsRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Create New Analytics",
                        summary = "Create analytics for a new event",
                        value = """
                            {
                                "eventId": 1,
                                "totalInvitations": 100,
                                "totalResponses": 75,
                                "goingCount": 50,
                                "maybeCount": 20,
                                "notGoingCount": 5,
                                "actualAttendees": 45,
                                "noShows": 5,
                                "engagementScore": 85.0,
                                "averageResponseTime": "3.2 days",
                                "topEngagementFactors": ["Location", "Timing"]
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Update Existing Analytics",
                        summary = "Update analytics with new attendance data",
                        value = """
                            {
                                "eventId": 1,
                                "actualAttendees": 48,
                                "noShows": 2,
                                "engagementScore": 88.0,
                                "topEngagementFactors": ["Location", "Timing", "Content", "Networking"]
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
            description = "Event analytics created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event analytics updated successfully",
                                "code": "201",
                                "data": {
                                    "eventId": 1,
                                    "totalInvitations": 100,
                                    "totalResponses": 75,
                                    "goingCount": 50,
                                    "maybeCount": 20,
                                    "notGoingCount": 5,
                                    "actualAttendees": 45,
                                    "noShows": 5,
                                    "responseRate": 75.0,
                                    "attendanceRate": 90.0,
                                    "engagementScore": 85.0,
                                    "averageResponseTime": "3.2 days",
                                    "topEngagementFactors": ["Location", "Timing"],
                                    "lastUpdated": "2024-12-01T16:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid analytics data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid analytics data provided",
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
    public ResponseEntity<ApiResponse<EventAnalyticsResponse>> updateEventAnalytics(
            @Valid @RequestBody EventAnalyticsRequest analyticsRequest) {
        EventAnalyticsResponse analytics = eventAnalyticsService.updateEventAnalytics(analyticsRequest);
        ApiResponse<EventAnalyticsResponse> response = new ApiResponse<>();
        response.setMessage("Event analytics updated successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/multiple")
    @Operation(
        summary = "Get Analytics for Multiple Events",
        description = "Retrieve analytics for multiple events by providing a list of event IDs",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Multiple event analytics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Multiple event analytics retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "eventId": 1,
                                        "totalInvitations": 150,
                                        "totalResponses": 120,
                                        "goingCount": 85,
                                        "maybeCount": 25,
                                        "notGoingCount": 10,
                                        "actualAttendees": 78,
                                        "noShows": 7,
                                        "responseRate": 80.0,
                                        "attendanceRate": 91.8,
                                        "engagementScore": 85.5,
                                        "averageResponseTime": "2.5 days",
                                        "topEngagementFactors": ["Location", "Timing", "Content"],
                                        "lastUpdated": "2024-12-01T15:30:00"
                                    },
                                    {
                                        "eventId": 2,
                                        "totalInvitations": 80,
                                        "totalResponses": 65,
                                        "goingCount": 45,
                                        "maybeCount": 15,
                                        "notGoingCount": 5,
                                        "actualAttendees": 42,
                                        "noShows": 3,
                                        "responseRate": 81.3,
                                        "attendanceRate": 93.3,
                                        "engagementScore": 87.2,
                                        "averageResponseTime": "1.8 days",
                                        "topEngagementFactors": ["Content", "Networking"],
                                        "lastUpdated": "2024-12-02T10:15:00"
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
            description = "Invalid event IDs provided",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid event IDs provided",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventAnalyticsResponse>>> getAnalyticsForEvents(
            @RequestParam List<Long> eventIds) {
        List<EventAnalyticsResponse> analytics = eventAnalyticsService.getAnalyticsForEvents(eventIds);
        ApiResponse<List<EventAnalyticsResponse>> response = new ApiResponse<>();
        response.setMessage("Multiple event analytics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get analytics in date range", description = "Retrieve analytics for events in a date range")
    public ResponseEntity<ApiResponse<List<EventAnalyticsResponse>>> getAnalyticsInDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<EventAnalyticsResponse> analytics = eventAnalyticsService.getAnalyticsInDateRange(startDate, endDate);
        ApiResponse<List<EventAnalyticsResponse>> response = new ApiResponse<>();
        response.setMessage("Date range analytics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/high-attendance")
    @Operation(summary = "Get analytics with high attendance", description = "Retrieve analytics for events with high attendance rates")
    public ResponseEntity<ApiResponse<List<EventAnalyticsResponse>>> getAnalyticsWithHighAttendance(
            @RequestParam(defaultValue = "80.0") Double minRate) {
        List<EventAnalyticsResponse> analytics = eventAnalyticsService.getAnalyticsWithHighAttendance(minRate);
        ApiResponse<List<EventAnalyticsResponse>> response = new ApiResponse<>();
        response.setMessage("High attendance analytics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/high-response")
    @Operation(summary = "Get analytics with high response", description = "Retrieve analytics for events with high response rates")
    public ResponseEntity<ApiResponse<List<EventAnalyticsResponse>>> getAnalyticsWithHighResponse(
            @RequestParam(defaultValue = "80.0") Double minRate) {
        List<EventAnalyticsResponse> analytics = eventAnalyticsService.getAnalyticsWithHighResponse(minRate);
        ApiResponse<List<EventAnalyticsResponse>> response = new ApiResponse<>();
        response.setMessage("High response analytics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve overall analytics statistics")
    public ResponseEntity<ApiResponse<Object>> getDashboardStats() {
        Double avgAttendanceRate = eventAnalyticsService.getAverageAttendanceRate();
        Double avgResponseRate = eventAnalyticsService.getAverageResponseRate();
        Long totalEventsWithAttendees = eventAnalyticsService.getTotalEventsWithAttendees();
        Long totalActualAttendees = eventAnalyticsService.getTotalActualAttendees();
        
        // Create a simple stats object using a Map
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("averageAttendanceRate", avgAttendanceRate);
        stats.put("averageResponseRate", avgResponseRate);
        stats.put("totalEventsWithAttendees", totalEventsWithAttendees);
        stats.put("totalActualAttendees", totalActualAttendees);
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setMessage("Dashboard statistics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(stats);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{eventId}/generate")
    @Operation(summary = "Generate analytics from participation", description = "Generate analytics from event participation data")
    public ResponseEntity<ApiResponse<EventAnalyticsResponse>> generateAnalyticsFromParticipation(@PathVariable Long eventId) {
        EventAnalyticsResponse analytics = eventAnalyticsService.generateAnalyticsFromParticipation(eventId);
        ApiResponse<EventAnalyticsResponse> response = new ApiResponse<>();
        response.setMessage("Analytics generated from participation data successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{eventId}/attendance")
    @Operation(summary = "Update attendance tracking", description = "Update actual attendance and no-shows for an event")
    public ResponseEntity<ApiResponse<EventAnalyticsResponse>> updateAttendanceTracking(
            @PathVariable Long eventId,
            @RequestParam Integer actualAttendees,
            @RequestParam Integer noShows) {
        EventAnalyticsResponse analytics = eventAnalyticsService.updateAttendanceTracking(eventId, actualAttendees, noShows);
        ApiResponse<EventAnalyticsResponse> response = new ApiResponse<>();
        response.setMessage("Attendance tracking updated successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/summary")
    @Operation(summary = "Get analytics summary", description = "Retrieve a summary of analytics for an event")
    public ResponseEntity<ApiResponse<EventAnalyticsResponse>> getAnalyticsSummary(@PathVariable Long eventId) {
        EventAnalyticsResponse analytics = eventAnalyticsService.getAnalyticsSummary(eventId);
        ApiResponse<EventAnalyticsResponse> response = new ApiResponse<>();
        response.setMessage("Analytics summary retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/export")
    @Operation(summary = "Export analytics data", description = "Export analytics data in various formats")
    public ResponseEntity<ApiResponse<String>> exportAnalyticsData(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "json") String format) {
        String exportedData = eventAnalyticsService.exportAnalyticsData(eventId, format);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Analytics data exported successfully");
        response.setCode("SUCCESS");
        response.setData(exportedData);
        return ResponseEntity.ok(response);
    }
} 