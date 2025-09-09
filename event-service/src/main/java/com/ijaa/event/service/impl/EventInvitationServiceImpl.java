package com.ijaa.event.service.impl;

import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.entity.EventInvitation;
import com.ijaa.event.domain.request.EventInvitationRequest;
import com.ijaa.event.domain.response.EventInvitationResponse;
import com.ijaa.event.repository.EventInvitationRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.EventInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventInvitationServiceImpl implements EventInvitationService {

    private final EventInvitationRepository eventInvitationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<EventInvitationResponse> sendInvitations(EventInvitationRequest request, String invitedByUsername) {
        // Validate event exists
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + request.getEventId()));
        
        // Check if user is the event creator
        if (!event.getCreatedByUsername().equals(invitedByUsername)) {
            throw new RuntimeException("Only event creator can send invitations");
        }
        
        List<EventInvitationResponse> responses = new ArrayList<>();
        
        for (String username : request.getUsernames()) {
            // Check if invitation already exists
            Optional<EventInvitation> existingInvitation = 
                eventInvitationRepository.findByEventIdAndInvitedUsername(request.getEventId(), username);
            
            if (existingInvitation.isPresent()) {
                throw new RuntimeException("Invitation already sent to user: " + username);
            }
            
            // Create new invitation
            EventInvitation invitation = new EventInvitation();
            invitation.setEventId(request.getEventId());
            invitation.setInvitedUsername(username);
            invitation.setInvitedByUsername(invitedByUsername);
            invitation.setPersonalMessage(request.getPersonalMessage());
            invitation.setIsRead(false);
            invitation.setIsResponded(false);
            
            EventInvitation savedInvitation = eventInvitationRepository.save(invitation);
            responses.add(createInvitationResponse(savedInvitation));
        }
        
        return responses;
    }

    @Override
    public void acceptInvitation(Long eventId, String invitedUsername) {
        EventInvitation invitation = eventInvitationRepository.findByEventIdAndInvitedUsername(eventId, invitedUsername)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        
        invitation.setIsResponded(true);
        invitation.setIsRead(true);
        eventInvitationRepository.save(invitation);
        
        // Note: Actual participation should be handled by EventParticipationService
    }

    @Override
    public void declineInvitation(Long eventId, String invitedUsername) {
        EventInvitation invitation = eventInvitationRepository.findByEventIdAndInvitedUsername(eventId, invitedUsername)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        
        invitation.setIsResponded(true);
        invitation.setIsRead(true);
        eventInvitationRepository.save(invitation);
    }

    @Override
    public void markInvitationAsRead(Long eventId, String invitedUsername) {
        EventInvitation invitation = eventInvitationRepository.findByEventIdAndInvitedUsername(eventId, invitedUsername)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        
        invitation.setIsRead(true);
        eventInvitationRepository.save(invitation);
    }

    @Override
    public List<EventInvitationResponse> getUserInvitations(String invitedUsername) {
        return eventInvitationRepository.findByInvitedUsername(invitedUsername).stream()
                .map(this::createInvitationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventInvitationResponse> getUnreadInvitations(String invitedUsername) {
        return eventInvitationRepository.findByInvitedUsernameAndIsReadFalse(invitedUsername).stream()
                .map(this::createInvitationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventInvitationResponse> getUnrespondedInvitations(String invitedUsername) {
        return eventInvitationRepository.findByInvitedUsernameAndIsRespondedFalse(invitedUsername).stream()
                .map(this::createInvitationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventInvitationResponse> getEventInvitations(Long eventId) {
        return eventInvitationRepository.findByEventId(eventId).stream()
                .map(this::createInvitationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventInvitationResponse> getInvitationsSentByUser(String invitedByUsername) {
        return eventInvitationRepository.findByInvitedByUsername(invitedByUsername).stream()
                .map(this::createInvitationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUnreadInvitations(String invitedUsername) {
        return eventInvitationRepository.countUnreadInvitations(invitedUsername);
    }

    @Override
    public Long countUnrespondedInvitations(String invitedUsername) {
        return eventInvitationRepository.countUnrespondedInvitations(invitedUsername);
    }

    @Override
    public boolean isUserInvited(Long eventId, String invitedUsername) {
        return eventInvitationRepository.findByEventIdAndInvitedUsername(eventId, invitedUsername).isPresent();
    }

    @Override
    public void deleteInvitation(Long eventId, String invitedUsername) {
        eventInvitationRepository.deleteByEventIdAndInvitedUsername(eventId, invitedUsername);
    }

    private EventInvitationResponse createInvitationResponse(EventInvitation invitation) {
        return new EventInvitationResponse(
                invitation.getId(),
                invitation.getEventId(),
                invitation.getInvitedUsername(),
                invitation.getInvitedByUsername(),
                invitation.getPersonalMessage(),
                invitation.getIsRead(),
                invitation.getIsResponded(),
                invitation.getCreatedAt(),
                invitation.getUpdatedAt()
        );
    }
} 
