package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventInvitationRepository extends JpaRepository<EventInvitation, Long> {
    
    // Find invitation by event and invited user
    Optional<EventInvitation> findByEventIdAndInvitedUsername(Long eventId, String invitedUsername);
    
    // Find all invitations for an event
    List<EventInvitation> findByEventId(Long eventId);
    
    // Find all invitations for a user
    List<EventInvitation> findByInvitedUsername(String invitedUsername);
    
    // Find unread invitations for a user
    List<EventInvitation> findByInvitedUsernameAndIsReadFalse(String invitedUsername);
    
    // Find unresponded invitations for a user
    List<EventInvitation> findByInvitedUsernameAndIsRespondedFalse(String invitedUsername);
    
    // Count unread invitations for a user
    @Query("SELECT COUNT(ei) FROM EventInvitation ei WHERE ei.invitedUsername = ?1 AND ei.isRead = false")
    Long countUnreadInvitations(String invitedUsername);
    
    // Count unresponded invitations for a user
    @Query("SELECT COUNT(ei) FROM EventInvitation ei WHERE ei.invitedUsername = ?1 AND ei.isResponded = false")
    Long countUnrespondedInvitations(String invitedUsername);
    
    // Find invitations sent by a user
    List<EventInvitation> findByInvitedByUsername(String invitedByUsername);
    
    // Delete invitation
    void deleteByEventIdAndInvitedUsername(Long eventId, String invitedUsername);
} 