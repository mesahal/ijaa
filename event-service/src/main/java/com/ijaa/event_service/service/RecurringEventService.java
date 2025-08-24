package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.request.RecurringEventRequest;
import com.ijaa.event_service.domain.response.RecurringEventResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RecurringEventService {
    
    // Get all recurring events
    List<RecurringEventResponse> getAllRecurringEvents();
    
    // Get all active recurring events
    List<RecurringEventResponse> getActiveRecurringEvents();
    
    // Get recurring event by ID
    RecurringEventResponse getRecurringEventById(Long eventId);
    
    // Create new recurring event
    RecurringEventResponse createRecurringEvent(RecurringEventRequest request);
    
    // Update existing recurring event
    RecurringEventResponse updateRecurringEvent(Long eventId, RecurringEventRequest request);
    
    // Delete recurring event
    void deleteRecurringEvent(Long eventId);
    
    // Activate recurring event
    RecurringEventResponse activateRecurringEvent(Long eventId);
    
    // Deactivate recurring event
    RecurringEventResponse deactivateRecurringEvent(Long eventId);
    
    // User-specific methods
    List<RecurringEventResponse> getRecurringEventsByUser(String username);
    
    List<RecurringEventResponse> getActiveRecurringEventsByUser(String username);
    
    RecurringEventResponse createRecurringEventForUser(RecurringEventRequest request, String username);
    
    RecurringEventResponse updateRecurringEventForUser(Long eventId, RecurringEventRequest request, String username);
    
    void deleteRecurringEventForUser(Long eventId, String username);
    
    RecurringEventResponse getRecurringEventByIdForUser(Long eventId, String username);
    
    // Search recurring events
    List<RecurringEventResponse> searchRecurringEvents(String location, String eventType, 
                                                      LocalDateTime startDate, LocalDateTime endDate, 
                                                      Boolean isOnline, String organizerName, 
                                                      String title, String description, String recurrenceType);
    
    // Generate event instances from recurring events
    void generateEventInstances();
    
    // Dashboard statistics
    Long getTotalRecurringEvents();
    Long getActiveRecurringEventsCount();
} 