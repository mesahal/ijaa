package com.ijaa.event.service;

import com.ijaa.event.domain.request.EventRequest;
import com.ijaa.event.domain.response.EventResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    
    // Get all events
    List<EventResponse> getAllEvents();
    
    // Get all active events
    List<EventResponse> getActiveEvents();
    
    // Get event by ID
    EventResponse getEventById(Long eventId);
    
    // Create new event
    EventResponse createEvent(EventRequest eventRequest);
    
    // Update existing event
    EventResponse updateEvent(Long eventId, EventRequest eventRequest);
    
    // Delete event
    void deleteEvent(Long eventId);
    
    // Activate event
    EventResponse activateEvent(Long eventId);
    
    // Deactivate event
    EventResponse deactivateEvent(Long eventId);
    
    // Dashboard statistics
    Long getTotalEvents();
    Long getActiveEventsCount();
    
    // User-specific methods
    // Get all events created by a specific user
    List<EventResponse> getEventsByUser(String username);
    
    // Get active events created by a specific user
    List<EventResponse> getActiveEventsByUser(String username);
    
    // Create event for a specific user
    EventResponse createEventForUser(EventRequest eventRequest, String username);
    
    // Update event for a specific user (only if they created it)
    EventResponse updateEventForUser(Long eventId, EventRequest eventRequest, String username);
    
    // Delete event for a specific user (only if they created it)
    void deleteEventForUser(Long eventId, String username);
    
    // Get event by ID for a specific user (only if they created it)
    EventResponse getEventByIdForUser(Long eventId, String username);
    
    // Search events
    List<EventResponse> searchEvents(String location, String eventType, LocalDateTime startDate, 
                                   LocalDateTime endDate, Boolean isOnline, String organizerName, 
                                   String title, String description);
    
    // Search events with pagination
    List<EventResponse> searchEventsWithPagination(String location, String eventType, LocalDateTime startDate, 
                                                 LocalDateTime endDate, Boolean isOnline, String organizerName, 
                                                 String title, String description, int page, int size);
} 
