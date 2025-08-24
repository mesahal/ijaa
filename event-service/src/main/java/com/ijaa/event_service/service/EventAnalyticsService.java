package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.request.EventAnalyticsRequest;
import com.ijaa.event_service.domain.response.EventAnalyticsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAnalyticsService {
    
    // Get analytics by event ID
    EventAnalyticsResponse getAnalyticsByEventId(Long eventId);
    
    // Create or update analytics
    EventAnalyticsResponse createOrUpdateAnalytics(EventAnalyticsRequest request);
    
    // Update analytics from event participation
    EventAnalyticsResponse updateAnalyticsFromParticipation(Long eventId);
    
    // Get analytics by organizer
    List<EventAnalyticsResponse> getAnalyticsByOrganizer(String organizerUsername);
    
    // Get completed analytics
    List<EventAnalyticsResponse> getCompletedAnalytics();
    
    // Get analytics by date range
    List<EventAnalyticsResponse> getAnalyticsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Get analytics by organizer and date range
    List<EventAnalyticsResponse> getAnalyticsByOrganizerAndDateRange(String organizerUsername, 
                                                                   LocalDateTime startDate, LocalDateTime endDate);
    
    // Get top performing events by attendance
    List<EventAnalyticsResponse> getTopPerformingEventsByAttendance();
    
    // Get top performing events by engagement
    List<EventAnalyticsResponse> getTopPerformingEventsByEngagement();
    
    // Get organizer statistics
    Double getAverageAttendanceRateByOrganizer(String organizerUsername);
    Double getAverageEngagementRateByOrganizer(String organizerUsername);
    Long getTotalEventsByOrganizer(String organizerUsername);
    Long getCompletedEventsByOrganizer(String organizerUsername);
    
    // Mark event as completed
    EventAnalyticsResponse markEventAsCompleted(Long eventId);
    
    // Calculate analytics for all events
    void calculateAnalyticsForAllEvents();
    
    // Additional methods for API compatibility
    EventAnalyticsResponse getEventAnalytics(Long eventId);
    EventAnalyticsResponse updateEventAnalytics(EventAnalyticsRequest request);
    List<EventAnalyticsResponse> getAnalyticsForEvents(List<Long> eventIds);
    List<EventAnalyticsResponse> getAnalyticsInDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<EventAnalyticsResponse> getAnalyticsWithHighAttendance(Double threshold);
    List<EventAnalyticsResponse> getAnalyticsWithHighResponse(Double threshold);
    Double getAverageAttendanceRate();
    Double getAverageResponseRate();
    Long getTotalEventsWithAttendees();
    Long getTotalActualAttendees();
    EventAnalyticsResponse generateAnalyticsFromParticipation(Long eventId);
    EventAnalyticsResponse updateAttendanceTracking(Long eventId, Integer totalInvited, Integer actualAttended);
    EventAnalyticsResponse getAnalyticsSummary(Long eventId);
    String exportAnalyticsData(Long eventId, String format);
} 