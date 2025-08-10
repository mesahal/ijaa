package com.ijaa.event_service.service.impl;


import com.ijaa.event_service.domain.entity.Event;
import com.ijaa.event_service.domain.request.EventRequest;
import com.ijaa.event_service.domain.response.EventResponse;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.service.EventService;
import com.ijaa.event_service.common.utils.FeatureFlagUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final FeatureFlagUtils featureFlagUtils;

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::createEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getActiveEvents() {
        return eventRepository.findByActiveTrueOrderByStartDateAsc().stream()
                .map(this::createEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return createEventResponse(event);
    }

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        // Validate dates
        if (eventRequest.getStartDate().isAfter(eventRequest.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        if (eventRequest.getStartDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start date cannot be in the past");
        }
        
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setLocation(eventRequest.getLocation());
        event.setEventType(eventRequest.getEventType());
        event.setActive(true);
        event.setIsOnline(eventRequest.getIsOnline() != null ? eventRequest.getIsOnline() : false);
        event.setMeetingLink(eventRequest.getMeetingLink());
        event.setMaxParticipants(eventRequest.getMaxParticipants());
        event.setCurrentParticipants(0);
        event.setOrganizerName(eventRequest.getOrganizerName());
        event.setOrganizerEmail(eventRequest.getOrganizerEmail());
        
        Event savedEvent = eventRepository.save(event);
        
        return createEventResponse(savedEvent);
    }

    @Override
    public EventResponse updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        // Validate dates
        if (eventRequest.getStartDate().isAfter(eventRequest.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        // Update event fields
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setLocation(eventRequest.getLocation());
        event.setEventType(eventRequest.getEventType());
        event.setIsOnline(eventRequest.getIsOnline() != null ? eventRequest.getIsOnline() : false);
        event.setMeetingLink(eventRequest.getMeetingLink());
        event.setMaxParticipants(eventRequest.getMaxParticipants());
        event.setOrganizerName(eventRequest.getOrganizerName());
        event.setOrganizerEmail(eventRequest.getOrganizerEmail());
        
        Event updatedEvent = eventRepository.save(event);
        return createEventResponse(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        eventRepository.delete(event);
    }

    @Override
    public EventResponse activateEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        event.setActive(true);
        Event updatedEvent = eventRepository.save(event);
        return createEventResponse(updatedEvent);
    }

    @Override
    public EventResponse deactivateEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        event.setActive(false);
        Event updatedEvent = eventRepository.save(event);
        return createEventResponse(updatedEvent);
    }

    @Override
    public Long getTotalEvents() {
        return eventRepository.count();
    }

    @Override
    public Long getActiveEventsCount() {
        return eventRepository.countByActiveTrue();
    }

    // User-specific methods implementation
    @Override
    public List<EventResponse> getEventsByUser(String username) {
        return eventRepository.findByCreatedByUsernameOrderByCreatedAtDesc(username).stream()
                .map(this::createEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getActiveEventsByUser(String username) {
        return eventRepository.findByCreatedByUsernameAndActiveTrueOrderByStartDateAsc(username).stream()
                .map(this::createEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse createEventForUser(EventRequest eventRequest, String username) {
        // Validate dates
        if (eventRequest.getStartDate().isAfter(eventRequest.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        if (eventRequest.getStartDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start date cannot be in the past");
        }
        
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setLocation(eventRequest.getLocation());
        event.setEventType(eventRequest.getEventType());
        event.setActive(true);
        event.setIsOnline(eventRequest.getIsOnline() != null ? eventRequest.getIsOnline() : false);
        event.setMeetingLink(eventRequest.getMeetingLink());
        event.setMaxParticipants(eventRequest.getMaxParticipants());
        event.setCurrentParticipants(0);
        event.setOrganizerName(eventRequest.getOrganizerName());
        event.setOrganizerEmail(eventRequest.getOrganizerEmail());
        event.setCreatedByUsername(username); // Set the user who created the event
        
        // Set privacy settings
        if (eventRequest.getPrivacy() != null) {
            event.setPrivacy(Event.EventPrivacy.valueOf(eventRequest.getPrivacy().toUpperCase()));
        } else {
            event.setPrivacy(Event.EventPrivacy.PUBLIC);
        }
        
        event.setInviteMessage(eventRequest.getInviteMessage());
        
        Event savedEvent = eventRepository.save(event);
        return createEventResponse(savedEvent);
    }

    @Override
    public EventResponse updateEventForUser(Long eventId, EventRequest eventRequest, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        // Check if the user created this event
        if (!username.equals(event.getCreatedByUsername())) {
            throw new RuntimeException("You can only update events that you created");
        }
        
        // Validate dates
        if (eventRequest.getStartDate().isAfter(eventRequest.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        // Update event fields
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setLocation(eventRequest.getLocation());
        event.setEventType(eventRequest.getEventType());
        event.setIsOnline(eventRequest.getIsOnline() != null ? eventRequest.getIsOnline() : false);
        event.setMeetingLink(eventRequest.getMeetingLink());
        event.setMaxParticipants(eventRequest.getMaxParticipants());
        event.setOrganizerName(eventRequest.getOrganizerName());
        event.setOrganizerEmail(eventRequest.getOrganizerEmail());
        
        // Update privacy settings
        if (eventRequest.getPrivacy() != null) {
            event.setPrivacy(Event.EventPrivacy.valueOf(eventRequest.getPrivacy().toUpperCase()));
        }
        
        event.setInviteMessage(eventRequest.getInviteMessage());
        
        Event updatedEvent = eventRepository.save(event);
        return createEventResponse(updatedEvent);
    }

    @Override
    public void deleteEventForUser(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        // Check if the user created this event
        if (!username.equals(event.getCreatedByUsername())) {
            throw new RuntimeException("You can only delete events that you created");
        }
        
        eventRepository.delete(event);
    }

    @Override
    public EventResponse getEventByIdForUser(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        // Check if the user created this event
        if (!username.equals(event.getCreatedByUsername())) {
            throw new RuntimeException("You can only view events that you created");
        }
        
        return createEventResponse(event);
    }

    private EventResponse createEventResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setStartDate(event.getStartDate());
        response.setEndDate(event.getEndDate());
        response.setLocation(event.getLocation());
        response.setEventType(event.getEventType());
        response.setActive(event.getActive());
        response.setIsOnline(event.getIsOnline());
        response.setMeetingLink(event.getMeetingLink());
        response.setMaxParticipants(event.getMaxParticipants());
        response.setCurrentParticipants(event.getCurrentParticipants());
        response.setOrganizerName(event.getOrganizerName());
        response.setOrganizerEmail(event.getOrganizerEmail());
        response.setCreatedByUsername(event.getCreatedByUsername());
        response.setPrivacy(event.getPrivacy() != null ? event.getPrivacy().name() : "PUBLIC");
        response.setInviteMessage(event.getInviteMessage());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());
        return response;
    }

    @Override
    public List<EventResponse> searchEvents(String location, String eventType, LocalDateTime startDate, 
                                          LocalDateTime endDate, Boolean isOnline, String organizerName, 
                                          String title, String description) {
        // Check if advanced search is enabled
        if (!featureFlagUtils.isAdvancedSearchEnabled()) {
            log.warn("Advanced search is disabled, returning basic search results");
            return getActiveEvents(); // Fallback to basic search
        }
        
        List<Event> events = eventRepository.searchEvents(location, eventType, startDate, endDate, 
                                                        isOnline, organizerName, title, description);
        List<EventResponse> results = events.stream()
                .map(this::createEventResponse)
                .collect(Collectors.toList());
        
        // Log feature usage
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.ADVANCED_SEARCH, null);
        
        return results;
    }

    @Override
    public List<EventResponse> searchEventsWithPagination(String location, String eventType, LocalDateTime startDate, 
                                                        LocalDateTime endDate, Boolean isOnline, String organizerName, 
                                                        String title, String description, int page, int size) {
        // For now, we'll use the same search method but could be enhanced with pagination
        return searchEvents(location, eventType, startDate, endDate, isOnline, organizerName, title, description);
    }
} 