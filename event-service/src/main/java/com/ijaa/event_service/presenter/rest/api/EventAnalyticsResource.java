package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.EventAnalyticsRequest;
import com.ijaa.event_service.domain.response.EventAnalyticsResponse;
import com.ijaa.event_service.service.EventAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event-analytics")
@Tag(name = "Event Analytics", description = "Event analytics and attendance tracking APIs")
public class EventAnalyticsResource {

    @Autowired
    private EventAnalyticsService eventAnalyticsService;

    @GetMapping("/{eventId}")
    @Operation(summary = "Get event analytics", description = "Retrieve analytics for a specific event")
    public ResponseEntity<ApiResponse<EventAnalyticsResponse>> getEventAnalytics(@PathVariable Long eventId) {
        EventAnalyticsResponse analytics = eventAnalyticsService.getEventAnalytics(eventId);
        ApiResponse<EventAnalyticsResponse> response = new ApiResponse<>();
        response.setMessage("Event analytics retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(analytics);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Update event analytics", description = "Create or update analytics for an event")
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
    @Operation(summary = "Get analytics for multiple events", description = "Retrieve analytics for multiple events")
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