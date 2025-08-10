package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.entity.RecurringEvent;
import com.ijaa.event_service.domain.request.RecurringEventRequest;
import com.ijaa.event_service.domain.response.RecurringEventResponse;
import com.ijaa.event_service.repository.RecurringEventRepository;
import com.ijaa.event_service.service.RecurringEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecurringEventServiceImpl implements RecurringEventService {

    private final RecurringEventRepository recurringEventRepository;

    @Override
    public List<RecurringEventResponse> getAllRecurringEvents() {
        return recurringEventRepository.findAll().stream()
                .map(this::createRecurringEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecurringEventResponse> getActiveRecurringEvents() {
        return recurringEventRepository.findByActiveTrue().stream()
                .map(this::createRecurringEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecurringEventResponse getRecurringEventById(Long eventId) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        return createRecurringEventResponse(recurringEvent);
    }

    @Override
    public RecurringEventResponse createRecurringEvent(RecurringEventRequest request) {
        RecurringEvent recurringEvent = new RecurringEvent();
        
        // Set basic event fields
        recurringEvent.setTitle(request.getTitle());
        recurringEvent.setDescription(request.getDescription());
        recurringEvent.setStartDate(request.getStartDate());
        recurringEvent.setEndDate(request.getEndDate());
        recurringEvent.setLocation(request.getLocation());
        recurringEvent.setEventType(request.getEventType());
        recurringEvent.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        recurringEvent.setMeetingLink(request.getMeetingLink());
        recurringEvent.setMaxParticipants(request.getMaxParticipants());
        recurringEvent.setOrganizerName(request.getOrganizerName());
        recurringEvent.setOrganizerEmail(request.getOrganizerEmail());
        recurringEvent.setInviteMessage(request.getInviteMessage());
        recurringEvent.setPrivacy(RecurringEvent.EventPrivacy.valueOf(request.getPrivacy()));
        
        // Set recurring event specific fields
        recurringEvent.setRecurrenceType(RecurringEvent.RecurrenceType.valueOf(request.getRecurrenceType()));
        recurringEvent.setRecurrenceInterval(request.getRecurrenceInterval());
        recurringEvent.setRecurrenceEndDate(request.getRecurrenceEndDate());
        recurringEvent.setRecurrenceDays(request.getRecurrenceDays());
        recurringEvent.setMaxOccurrences(request.getMaxOccurrences());
        recurringEvent.setGenerateInstances(request.getGenerateInstances() != null ? request.getGenerateInstances() : true);
        
        RecurringEvent savedEvent = recurringEventRepository.save(recurringEvent);
        return createRecurringEventResponse(savedEvent);
    }

    @Override
    public RecurringEventResponse updateRecurringEvent(Long eventId, RecurringEventRequest request) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        // Update event fields
        recurringEvent.setTitle(request.getTitle());
        recurringEvent.setDescription(request.getDescription());
        recurringEvent.setStartDate(request.getStartDate());
        recurringEvent.setEndDate(request.getEndDate());
        recurringEvent.setLocation(request.getLocation());
        recurringEvent.setEventType(request.getEventType());
        recurringEvent.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        recurringEvent.setMeetingLink(request.getMeetingLink());
        recurringEvent.setMaxParticipants(request.getMaxParticipants());
        recurringEvent.setOrganizerName(request.getOrganizerName());
        recurringEvent.setOrganizerEmail(request.getOrganizerEmail());
        recurringEvent.setInviteMessage(request.getInviteMessage());
        recurringEvent.setPrivacy(RecurringEvent.EventPrivacy.valueOf(request.getPrivacy()));
        
        // Update recurring event specific fields
        recurringEvent.setRecurrenceType(RecurringEvent.RecurrenceType.valueOf(request.getRecurrenceType()));
        recurringEvent.setRecurrenceInterval(request.getRecurrenceInterval());
        recurringEvent.setRecurrenceEndDate(request.getRecurrenceEndDate());
        recurringEvent.setRecurrenceDays(request.getRecurrenceDays());
        recurringEvent.setMaxOccurrences(request.getMaxOccurrences());
        recurringEvent.setGenerateInstances(request.getGenerateInstances() != null ? request.getGenerateInstances() : true);
        
        RecurringEvent updatedEvent = recurringEventRepository.save(recurringEvent);
        return createRecurringEventResponse(updatedEvent);
    }

    @Override
    public void deleteRecurringEvent(Long eventId) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        recurringEventRepository.delete(recurringEvent);
    }

    @Override
    public RecurringEventResponse activateRecurringEvent(Long eventId) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        recurringEvent.setActive(true);
        RecurringEvent updatedEvent = recurringEventRepository.save(recurringEvent);
        return createRecurringEventResponse(updatedEvent);
    }

    @Override
    public RecurringEventResponse deactivateRecurringEvent(Long eventId) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        recurringEvent.setActive(false);
        RecurringEvent updatedEvent = recurringEventRepository.save(recurringEvent);
        return createRecurringEventResponse(updatedEvent);
    }

    @Override
    public List<RecurringEventResponse> getRecurringEventsByUser(String username) {
        return recurringEventRepository.findByCreatedByUsername(username).stream()
                .map(this::createRecurringEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecurringEventResponse> getActiveRecurringEventsByUser(String username) {
        return recurringEventRepository.findByCreatedByUsernameAndActiveTrue(username).stream()
                .map(this::createRecurringEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecurringEventResponse createRecurringEventForUser(RecurringEventRequest request, String username) {
        RecurringEvent recurringEvent = new RecurringEvent();
        
        // Set basic event fields
        recurringEvent.setTitle(request.getTitle());
        recurringEvent.setDescription(request.getDescription());
        recurringEvent.setStartDate(request.getStartDate());
        recurringEvent.setEndDate(request.getEndDate());
        recurringEvent.setLocation(request.getLocation());
        recurringEvent.setEventType(request.getEventType());
        recurringEvent.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        recurringEvent.setMeetingLink(request.getMeetingLink());
        recurringEvent.setMaxParticipants(request.getMaxParticipants());
        recurringEvent.setOrganizerName(request.getOrganizerName());
        recurringEvent.setOrganizerEmail(request.getOrganizerEmail());
        recurringEvent.setInviteMessage(request.getInviteMessage());
        recurringEvent.setPrivacy(RecurringEvent.EventPrivacy.valueOf(request.getPrivacy()));
        recurringEvent.setCreatedByUsername(username);
        
        // Set recurring event specific fields
        recurringEvent.setRecurrenceType(RecurringEvent.RecurrenceType.valueOf(request.getRecurrenceType()));
        recurringEvent.setRecurrenceInterval(request.getRecurrenceInterval());
        recurringEvent.setRecurrenceEndDate(request.getRecurrenceEndDate());
        recurringEvent.setRecurrenceDays(request.getRecurrenceDays());
        recurringEvent.setMaxOccurrences(request.getMaxOccurrences());
        recurringEvent.setGenerateInstances(request.getGenerateInstances() != null ? request.getGenerateInstances() : true);
        
        RecurringEvent savedEvent = recurringEventRepository.save(recurringEvent);
        return createRecurringEventResponse(savedEvent);
    }

    @Override
    public RecurringEventResponse updateRecurringEventForUser(Long eventId, RecurringEventRequest request, String username) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        // Check if user owns this event
        if (!username.equals(recurringEvent.getCreatedByUsername())) {
            throw new RuntimeException("You can only update your own recurring events");
        }
        
        return updateRecurringEvent(eventId, request);
    }

    @Override
    public void deleteRecurringEventForUser(Long eventId, String username) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        // Check if user owns this event
        if (!username.equals(recurringEvent.getCreatedByUsername())) {
            throw new RuntimeException("You can only delete your own recurring events");
        }
        
        deleteRecurringEvent(eventId);
    }

    @Override
    public RecurringEventResponse getRecurringEventByIdForUser(Long eventId, String username) {
        RecurringEvent recurringEvent = recurringEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Recurring event not found with id: " + eventId));
        
        // Check if user owns this event
        if (!username.equals(recurringEvent.getCreatedByUsername())) {
            throw new RuntimeException("You can only view your own recurring events");
        }
        
        return createRecurringEventResponse(recurringEvent);
    }

    @Override
    public List<RecurringEventResponse> searchRecurringEvents(String location, String eventType, 
                                                             LocalDateTime startDate, LocalDateTime endDate, 
                                                             Boolean isOnline, String organizerName, 
                                                             String title, String description, String recurrenceType) {
        // This is a simplified search implementation
        // In a real application, you would implement more sophisticated search logic
        List<RecurringEvent> events = recurringEventRepository.findByActiveTrue();
        
        return events.stream()
                .filter(event -> location == null || (event.getLocation() != null && event.getLocation().contains(location)))
                .filter(event -> eventType == null || (event.getEventType() != null && event.getEventType().equals(eventType)))
                .filter(event -> startDate == null || event.getStartDate().isAfter(startDate))
                .filter(event -> endDate == null || event.getEndDate().isBefore(endDate))
                .filter(event -> isOnline == null || event.getIsOnline().equals(isOnline))
                .filter(event -> organizerName == null || (event.getOrganizerName() != null && event.getOrganizerName().contains(organizerName)))
                .filter(event -> title == null || (event.getTitle() != null && event.getTitle().contains(title)))
                .filter(event -> description == null || (event.getDescription() != null && event.getDescription().contains(description)))
                .filter(event -> recurrenceType == null || event.getRecurrenceType().toString().equals(recurrenceType))
                .map(this::createRecurringEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void generateEventInstances() {
        // This method would generate individual event instances from recurring events
        // Implementation would depend on the specific business logic
        log.info("Generating event instances from recurring events");
        
        List<RecurringEvent> recurringEvents = recurringEventRepository.findActiveRecurringEventsWithInstanceGeneration();
        
        for (RecurringEvent recurringEvent : recurringEvents) {
            // Generate instances based on recurrence pattern
            // This is a placeholder implementation
            log.info("Generating instances for recurring event: {}", recurringEvent.getTitle());
        }
    }

    @Override
    public Long getTotalRecurringEvents() {
        return recurringEventRepository.count();
    }

    @Override
    public Long getActiveRecurringEventsCount() {
        return recurringEventRepository.countByActiveTrue();
    }

    private RecurringEventResponse createRecurringEventResponse(RecurringEvent recurringEvent) {
        RecurringEventResponse response = new RecurringEventResponse();
        response.setId(recurringEvent.getId());
        response.setTitle(recurringEvent.getTitle());
        response.setDescription(recurringEvent.getDescription());
        response.setStartDate(recurringEvent.getStartDate());
        response.setEndDate(recurringEvent.getEndDate());
        response.setLocation(recurringEvent.getLocation());
        response.setEventType(recurringEvent.getEventType());
        response.setActive(recurringEvent.getActive());
        response.setIsOnline(recurringEvent.getIsOnline());
        response.setMeetingLink(recurringEvent.getMeetingLink());
        response.setMaxParticipants(recurringEvent.getMaxParticipants());
        response.setCurrentParticipants(recurringEvent.getCurrentParticipants());
        response.setOrganizerName(recurringEvent.getOrganizerName());
        response.setOrganizerEmail(recurringEvent.getOrganizerEmail());
        response.setCreatedByUsername(recurringEvent.getCreatedByUsername());
        response.setPrivacy(recurringEvent.getPrivacy().toString());
        response.setInviteMessage(recurringEvent.getInviteMessage());
        response.setRecurrenceType(recurringEvent.getRecurrenceType().toString());
        response.setRecurrenceInterval(recurringEvent.getRecurrenceInterval());
        response.setRecurrenceEndDate(recurringEvent.getRecurrenceEndDate());
        response.setRecurrenceDays(recurringEvent.getRecurrenceDays());
        response.setMaxOccurrences(recurringEvent.getMaxOccurrences());
        response.setGenerateInstances(recurringEvent.getGenerateInstances());
        response.setCreatedAt(recurringEvent.getCreatedAt());
        response.setUpdatedAt(recurringEvent.getUpdatedAt());
        
        return response;
    }
} 