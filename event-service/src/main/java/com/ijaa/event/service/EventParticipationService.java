package com.ijaa.event.service;

import com.ijaa.event.domain.request.EventParticipationRequest;
import com.ijaa.event.domain.response.EventParticipationResponse;

import java.util.List;

public interface EventParticipationService {
    
    // RSVP to an event
    EventParticipationResponse rsvpToEvent(EventParticipationRequest request, String username);
    
    // Update RSVP status
    EventParticipationResponse updateRsvp(Long eventId, String status, String message, String username);
    
    // Cancel RSVP
    void cancelRsvp(Long eventId, String username);
    
    // Get user's participation for an event
    EventParticipationResponse getUserParticipation(Long eventId, String username);
    
    // Get all participations for an event
    List<EventParticipationResponse> getEventParticipations(Long eventId);
    
    // Get all participations for a user
    List<EventParticipationResponse> getUserParticipations(String username);
    
    // Get participations by status for an event
    List<EventParticipationResponse> getEventParticipationsByStatus(Long eventId, String status);
    
    // Count participations by status for an event
    Long countParticipationsByStatus(Long eventId, String status);
    
    // Check if user is participating in an event
    boolean isUserParticipating(Long eventId, String username);
    
    // Get participation status for user
    String getUserParticipationStatus(Long eventId, String username);
} 
