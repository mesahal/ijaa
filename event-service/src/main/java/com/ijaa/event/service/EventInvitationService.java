package com.ijaa.event.service;

import com.ijaa.event.domain.request.EventInvitationRequest;
import com.ijaa.event.domain.response.EventInvitationResponse;

import java.util.List;

public interface EventInvitationService {
    
    // Send invitations to users
    List<EventInvitationResponse> sendInvitations(EventInvitationRequest request, String invitedByUsername);
    
    // Accept invitation
    void acceptInvitation(Long eventId, String invitedUsername);
    
    // Decline invitation
    void declineInvitation(Long eventId, String invitedUsername);
    
    // Mark invitation as read
    void markInvitationAsRead(Long eventId, String invitedUsername);
    
    // Get all invitations for a user
    List<EventInvitationResponse> getUserInvitations(String invitedUsername);
    
    // Get unread invitations for a user
    List<EventInvitationResponse> getUnreadInvitations(String invitedUsername);
    
    // Get unresponded invitations for a user
    List<EventInvitationResponse> getUnrespondedInvitations(String invitedUsername);
    
    // Get all invitations for an event
    List<EventInvitationResponse> getEventInvitations(Long eventId);
    
    // Get invitations sent by a user
    List<EventInvitationResponse> getInvitationsSentByUser(String invitedByUsername);
    
    // Count unread invitations for a user
    Long countUnreadInvitations(String invitedUsername);
    
    // Count unresponded invitations for a user
    Long countUnrespondedInvitations(String invitedUsername);
    
    // Check if user is invited to an event
    boolean isUserInvited(Long eventId, String invitedUsername);
    
    // Delete invitation
    void deleteInvitation(Long eventId, String invitedUsername);
} 
