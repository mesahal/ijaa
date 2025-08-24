package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.entity.EventAnalytics;
import com.ijaa.event_service.domain.request.EventAnalyticsRequest;
import com.ijaa.event_service.domain.response.EventAnalyticsResponse;
import com.ijaa.event_service.repository.EventAnalyticsRepository;
import com.ijaa.event_service.service.EventAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAnalyticsServiceImpl implements EventAnalyticsService {

    private final EventAnalyticsRepository eventAnalyticsRepository;

    @Override
    public EventAnalyticsResponse getAnalyticsByEventId(Long eventId) {
        Optional<EventAnalytics> analytics = eventAnalyticsRepository.findByEventId(eventId);
        
        if (analytics.isPresent()) {
            return createEventAnalyticsResponse(analytics.get());
        } else {
            // Create default analytics if not found
            EventAnalytics defaultAnalytics = new EventAnalytics();
            defaultAnalytics.setEventId(eventId);
            defaultAnalytics.setEventTitle("Unknown Event");
            defaultAnalytics.setOrganizerUsername("Unknown");
            defaultAnalytics.setTotalInvitations(0);
            defaultAnalytics.setConfirmedAttendees(0);
            defaultAnalytics.setMaybeAttendees(0);
            defaultAnalytics.setDeclinedAttendees(0);
            defaultAnalytics.setPendingResponses(0);
            defaultAnalytics.setTotalComments(0);
            defaultAnalytics.setTotalMediaUploads(0);
            defaultAnalytics.setTotalReminders(0);
            defaultAnalytics.setAttendanceRate(0.0);
            defaultAnalytics.setEngagementRate(0.0);
            defaultAnalytics.setIsCompleted(false);
            
            EventAnalytics savedAnalytics = eventAnalyticsRepository.save(defaultAnalytics);
            return createEventAnalyticsResponse(savedAnalytics);
        }
    }

    @Override
    public EventAnalyticsResponse createOrUpdateAnalytics(EventAnalyticsRequest request) {
        Optional<EventAnalytics> existingAnalytics = eventAnalyticsRepository.findByEventId(request.getEventId());
        
        EventAnalytics analytics;
        if (existingAnalytics.isPresent()) {
            analytics = existingAnalytics.get();
        } else {
            analytics = new EventAnalytics();
            analytics.setEventId(request.getEventId());
        }
        
        // Update analytics fields
        analytics.setEventTitle(request.getEventTitle());
        analytics.setOrganizerUsername(request.getOrganizerUsername());
        analytics.setTotalInvitations(request.getTotalInvitations());
        analytics.setConfirmedAttendees(request.getConfirmedAttendees());
        analytics.setMaybeAttendees(request.getMaybeAttendees());
        analytics.setDeclinedAttendees(request.getDeclinedAttendees());
        analytics.setPendingResponses(request.getPendingResponses());
        analytics.setTotalComments(request.getTotalComments());
        analytics.setTotalMediaUploads(request.getTotalMediaUploads());
        analytics.setTotalReminders(request.getTotalReminders());
        analytics.setAttendanceRate(request.getAttendanceRate());
        analytics.setEngagementRate(request.getEngagementRate());
        analytics.setIsCompleted(request.getIsCompleted());
        analytics.setEventStartDate(request.getEventStartDate());
        analytics.setEventEndDate(request.getEventEndDate());
        
        // Calculate rates if not provided
        if (analytics.getAttendanceRate() == null || analytics.getAttendanceRate() == 0.0) {
            if (analytics.getTotalInvitations() > 0) {
                analytics.setAttendanceRate((double) analytics.getConfirmedAttendees() / analytics.getTotalInvitations() * 100);
            }
        }
        
        if (analytics.getEngagementRate() == null || analytics.getEngagementRate() == 0.0) {
            int totalParticipants = analytics.getConfirmedAttendees() + analytics.getMaybeAttendees();
            if (totalParticipants > 0) {
                int totalEngagement = analytics.getTotalComments() + analytics.getTotalMediaUploads();
                analytics.setEngagementRate((double) totalEngagement / totalParticipants * 100);
            }
        }
        
        EventAnalytics savedAnalytics = eventAnalyticsRepository.save(analytics);
        return createEventAnalyticsResponse(savedAnalytics);
    }

    @Override
    public EventAnalyticsResponse updateAnalyticsFromParticipation(Long eventId) {
        // This method would update analytics based on actual participation data
        // For now, we'll just return the current analytics
        return getAnalyticsByEventId(eventId);
    }

    @Override
    public List<EventAnalyticsResponse> getAnalyticsByOrganizer(String organizerUsername) {
        return eventAnalyticsRepository.findByOrganizerUsername(organizerUsername).stream()
                .map(this::createEventAnalyticsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventAnalyticsResponse> getCompletedAnalytics() {
        return eventAnalyticsRepository.findByIsCompletedTrue().stream()
                .map(this::createEventAnalyticsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventAnalyticsResponse> getAnalyticsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventAnalyticsRepository.findAnalyticsByDateRange(startDate, endDate).stream()
                .map(this::createEventAnalyticsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventAnalyticsResponse> getAnalyticsByOrganizerAndDateRange(String organizerUsername, 
                                                                         LocalDateTime startDate, LocalDateTime endDate) {
        return eventAnalyticsRepository.findAnalyticsByOrganizerAndDateRange(organizerUsername, startDate, endDate).stream()
                .map(this::createEventAnalyticsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventAnalyticsResponse> getTopPerformingEventsByAttendance() {
        return eventAnalyticsRepository.findTopPerformingEventsByAttendance().stream()
                .map(this::createEventAnalyticsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventAnalyticsResponse> getTopPerformingEventsByEngagement() {
        return eventAnalyticsRepository.findTopPerformingEventsByEngagement().stream()
                .map(this::createEventAnalyticsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageAttendanceRateByOrganizer(String organizerUsername) {
        Double rate = eventAnalyticsRepository.getAverageAttendanceRateByOrganizer(organizerUsername);
        return rate != null ? rate : 0.0;
    }

    @Override
    public Double getAverageEngagementRateByOrganizer(String organizerUsername) {
        Double rate = eventAnalyticsRepository.getAverageEngagementRateByOrganizer(organizerUsername);
        return rate != null ? rate : 0.0;
    }

    @Override
    public Long getTotalEventsByOrganizer(String organizerUsername) {
        return eventAnalyticsRepository.countByOrganizerUsername(organizerUsername);
    }

    @Override
    public Long getCompletedEventsByOrganizer(String organizerUsername) {
        return eventAnalyticsRepository.countByOrganizerUsernameAndIsCompletedTrue(organizerUsername);
    }

    @Override
    public EventAnalyticsResponse markEventAsCompleted(Long eventId) {
        Optional<EventAnalytics> analytics = eventAnalyticsRepository.findByEventId(eventId);
        
        if (analytics.isPresent()) {
            EventAnalytics eventAnalytics = analytics.get();
            eventAnalytics.setIsCompleted(true);
            EventAnalytics savedAnalytics = eventAnalyticsRepository.save(eventAnalytics);
            return createEventAnalyticsResponse(savedAnalytics);
        } else {
            throw new RuntimeException("Event analytics not found for event id: " + eventId);
        }
    }

    @Override
    public void calculateAnalyticsForAllEvents() {
        // This method would calculate analytics for all events
        // Implementation would depend on the specific business logic
        log.info("Calculating analytics for all events");
        
        List<EventAnalytics> allAnalytics = eventAnalyticsRepository.findAll();
        
        for (EventAnalytics analytics : allAnalytics) {
            // Recalculate rates
            if (analytics.getTotalInvitations() > 0) {
                analytics.setAttendanceRate((double) analytics.getConfirmedAttendees() / analytics.getTotalInvitations() * 100);
            }
            
            int totalParticipants = analytics.getConfirmedAttendees() + analytics.getMaybeAttendees();
            if (totalParticipants > 0) {
                int totalEngagement = analytics.getTotalComments() + analytics.getTotalMediaUploads();
                analytics.setEngagementRate((double) totalEngagement / totalParticipants * 100);
            }
            
            eventAnalyticsRepository.save(analytics);
        }
    }

    private EventAnalyticsResponse createEventAnalyticsResponse(EventAnalytics analytics) {
        EventAnalyticsResponse response = new EventAnalyticsResponse();
        response.setId(analytics.getId());
        response.setEventId(analytics.getEventId());
        response.setEventTitle(analytics.getEventTitle());
        response.setOrganizerUsername(analytics.getOrganizerUsername());
        response.setTotalInvitations(analytics.getTotalInvitations());
        response.setConfirmedAttendees(analytics.getConfirmedAttendees());
        response.setMaybeAttendees(analytics.getMaybeAttendees());
        response.setDeclinedAttendees(analytics.getDeclinedAttendees());
        response.setPendingResponses(analytics.getPendingResponses());
        response.setTotalComments(analytics.getTotalComments());
        response.setTotalMediaUploads(analytics.getTotalMediaUploads());
        response.setTotalReminders(analytics.getTotalReminders());
        response.setFirstRsvpTime(analytics.getFirstRsvpTime());
        response.setLastRsvpTime(analytics.getLastRsvpTime());
        response.setAverageResponseTimeHours(analytics.getAverageResponseTimeHours());
        response.setAttendanceRate(analytics.getAttendanceRate());
        response.setEngagementRate(analytics.getEngagementRate());
        response.setIsCompleted(analytics.getIsCompleted());
        response.setEventStartDate(analytics.getEventStartDate());
        response.setEventEndDate(analytics.getEventEndDate());
        response.setCreatedAt(analytics.getCreatedAt());
        response.setUpdatedAt(analytics.getUpdatedAt());
        
        return response;
    }
    
    // Additional methods for API compatibility
    @Override
    public EventAnalyticsResponse getEventAnalytics(Long eventId) {
        return getAnalyticsByEventId(eventId);
    }
    
    @Override
    public EventAnalyticsResponse updateEventAnalytics(EventAnalyticsRequest request) {
        return createOrUpdateAnalytics(request);
    }
    
    @Override
    public List<EventAnalyticsResponse> getAnalyticsForEvents(List<Long> eventIds) {
        return eventIds.stream()
                .map(this::getAnalyticsByEventId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EventAnalyticsResponse> getAnalyticsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return getAnalyticsByDateRange(startDate, endDate);
    }
    
    @Override
    public List<EventAnalyticsResponse> getAnalyticsWithHighAttendance(Double threshold) {
        return getTopPerformingEventsByAttendance().stream()
                .filter(analytics -> analytics.getAttendanceRate() >= threshold)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EventAnalyticsResponse> getAnalyticsWithHighResponse(Double threshold) {
        return getTopPerformingEventsByEngagement().stream()
                .filter(analytics -> analytics.getEngagementRate() >= threshold)
                .collect(Collectors.toList());
    }
    
    @Override
    public Double getAverageAttendanceRate() {
        List<EventAnalyticsResponse> allAnalytics = getCompletedAnalytics();
        if (allAnalytics.isEmpty()) {
            return 0.0;
        }
        return allAnalytics.stream()
                .mapToDouble(EventAnalyticsResponse::getAttendanceRate)
                .average()
                .orElse(0.0);
    }
    
    @Override
    public Double getAverageResponseRate() {
        List<EventAnalyticsResponse> allAnalytics = getCompletedAnalytics();
        if (allAnalytics.isEmpty()) {
            return 0.0;
        }
        return allAnalytics.stream()
                .mapToDouble(EventAnalyticsResponse::getEngagementRate)
                .average()
                .orElse(0.0);
    }
    
    @Override
    public Long getTotalEventsWithAttendees() {
        return (long) getCompletedAnalytics().size();
    }
    
    @Override
    public Long getTotalActualAttendees() {
        return getCompletedAnalytics().stream()
                .mapToLong(EventAnalyticsResponse::getConfirmedAttendees)
                .sum();
    }
    
    @Override
    public EventAnalyticsResponse generateAnalyticsFromParticipation(Long eventId) {
        return updateAnalyticsFromParticipation(eventId);
    }
    
    @Override
    public EventAnalyticsResponse updateAttendanceTracking(Long eventId, Integer totalInvited, Integer actualAttended) {
        EventAnalytics analytics = eventAnalyticsRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event analytics not found for event: " + eventId));
        
        analytics.setTotalInvitations(totalInvited);
        analytics.setConfirmedAttendees(actualAttended);
        analytics.setAttendanceRate(actualAttended.doubleValue() / totalInvited.doubleValue() * 100);
        
        EventAnalytics updatedAnalytics = eventAnalyticsRepository.save(analytics);
        return createEventAnalyticsResponse(updatedAnalytics);
    }
    
    @Override
    public EventAnalyticsResponse getAnalyticsSummary(Long eventId) {
        return getAnalyticsByEventId(eventId);
    }
    
    @Override
    public String exportAnalyticsData(Long eventId, String format) {
        EventAnalyticsResponse analytics = getAnalyticsByEventId(eventId);
        // Simulate export functionality
        return "Analytics data exported in " + format + " format for event: " + eventId;
    }
} 