package com.ijaa.event.service.impl;

import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.entity.EventParticipation;
import com.ijaa.event.domain.request.EventParticipationRequest;
import com.ijaa.event.domain.response.EventParticipationResponse;
import com.ijaa.event.repository.EventParticipationRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.EventParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventParticipationServiceImpl implements EventParticipationService {

    private final EventParticipationRepository eventParticipationRepository;
    private final EventRepository eventRepository;

    @Override
    public EventParticipationResponse rsvpToEvent(EventParticipationRequest request, String username) {
        // Validate event exists
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + request.getEventId()));
        
        // Check if event is active
        if (!event.getActive()) {
            throw new RuntimeException("Event is not active");
        }
        
        // Check if user is already participating
        Optional<EventParticipation> existingParticipation = 
            eventParticipationRepository.findByEventIdAndParticipantUsername(request.getEventId(), username);
        
        if (existingParticipation.isPresent()) {
            throw new RuntimeException("User is already participating in this event");
        }
        
        // Create new participation
        EventParticipation participation = new EventParticipation();
        participation.setEventId(request.getEventId());
        participation.setParticipantUsername(username);
        participation.setStatus(mapIncomingStatus(request.getStatus()));
        participation.setMessage(request.getMessage());
        
        EventParticipation savedParticipation = eventParticipationRepository.save(participation);
        
        // Update event participant count if status is GOING
        if (savedParticipation.getStatus() == EventParticipation.ParticipationStatus.GOING) {
            updateEventParticipantCount(request.getEventId());
        }
        
        return createParticipationResponse(savedParticipation);
    }

    @Override
    public EventParticipationResponse updateRsvp(Long eventId, String status, String message, String username) {
        EventParticipation participation = eventParticipationRepository.findByEventIdAndParticipantUsername(eventId, username)
                .orElseThrow(() -> new RuntimeException("Participation not found"));
        
        EventParticipation.ParticipationStatus oldStatus = participation.getStatus();
        participation.setStatus(mapIncomingStatus(status));
        participation.setMessage(message);
        
        EventParticipation savedParticipation = eventParticipationRepository.save(participation);
        
        // Update event participant count if status changed
        if (oldStatus != savedParticipation.getStatus()) {
            updateEventParticipantCount(eventId);
        }
        
        return createParticipationResponse(savedParticipation);
    }

    @Override
    public void cancelRsvp(Long eventId, String username) {
        EventParticipation participation = eventParticipationRepository.findByEventIdAndParticipantUsername(eventId, username)
                .orElseThrow(() -> new RuntimeException("Participation not found"));
        
        eventParticipationRepository.delete(participation);
        
        // Update event participant count
        updateEventParticipantCount(eventId);
    }

    @Override
    public EventParticipationResponse getUserParticipation(Long eventId, String username) {
        Optional<EventParticipation> participation = eventParticipationRepository.findByEventIdAndParticipantUsername(eventId, username);
        return participation.map(this::createParticipationResponse).orElse(null);
    }

    @Override
    public List<EventParticipationResponse> getEventParticipations(Long eventId) {
        return eventParticipationRepository.findByEventId(eventId).stream()
                .map(this::createParticipationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventParticipationResponse> getUserParticipations(String username) {
        return eventParticipationRepository.findByParticipantUsername(username).stream()
                .map(this::createParticipationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventParticipationResponse> getEventParticipationsByStatus(Long eventId, String status) {
        EventParticipation.ParticipationStatus participationStatus = mapIncomingStatus(status);
        return eventParticipationRepository.findByEventIdAndStatus(eventId, participationStatus).stream()
                .map(this::createParticipationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long countParticipationsByStatus(Long eventId, String status) {
        EventParticipation.ParticipationStatus participationStatus = mapIncomingStatus(status);
        return eventParticipationRepository.countByEventIdAndStatus(eventId, participationStatus);
    }

    @Override
    public boolean isUserParticipating(Long eventId, String username) {
        return eventParticipationRepository.findByEventIdAndParticipantUsername(eventId, username).isPresent();
    }

    @Override
    public String getUserParticipationStatus(Long eventId, String username) {
        Optional<EventParticipation> participation = eventParticipationRepository.findByEventIdAndParticipantUsername(eventId, username);
        return participation.map(p -> mapOutgoingStatus(p.getStatus())).orElse(null);
    }

    private EventParticipation.ParticipationStatus mapIncomingStatus(String status) {
        if (status == null) {
            throw new RuntimeException("Invalid status. Must be CONFIRMED, MAYBE, or DECLINED");
        }
        switch (status) { // case-sensitive, no trimming
            case "CONFIRMED":
                return EventParticipation.ParticipationStatus.GOING;
            case "MAYBE":
                return EventParticipation.ParticipationStatus.MAYBE;
            case "DECLINED":
                return EventParticipation.ParticipationStatus.NOT_GOING;
            default:
                throw new RuntimeException("Invalid status. Must be CONFIRMED, MAYBE, or DECLINED");
        }
    }

    private String mapOutgoingStatus(EventParticipation.ParticipationStatus status) {
        if (status == null) return null;
        switch (status) {
            case GOING:
                return "CONFIRMED";
            case MAYBE:
                return "MAYBE";
            case NOT_GOING:
                return "DECLINED";
            case PENDING:
                return "MAYBE"; // normalize legacy pending to supported value
            default:
                return null;
        }
    }

    private EventParticipationResponse createParticipationResponse(EventParticipation participation) {
        return new EventParticipationResponse(
                participation.getId(),
                participation.getEventId(),
                participation.getParticipantUsername(),
                mapOutgoingStatus(participation.getStatus()),
                participation.getMessage(),
                participation.getCreatedAt(),
                participation.getUpdatedAt()
        );
    }

    private void updateEventParticipantCount(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            Long goingCount = eventParticipationRepository.countByEventIdAndStatus(eventId, EventParticipation.ParticipationStatus.GOING);
            event.setCurrentParticipants(goingCount.intValue());
            eventRepository.save(event);
        }
    }
} 
