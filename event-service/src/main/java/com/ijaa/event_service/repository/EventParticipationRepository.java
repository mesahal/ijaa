package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    
    // Find participation by event and participant
    Optional<EventParticipation> findByEventIdAndParticipantUsername(Long eventId, String participantUsername);
    
    // Find all participations for an event
    List<EventParticipation> findByEventId(Long eventId);
    
    // Find all participations for a participant
    List<EventParticipation> findByParticipantUsername(String participantUsername);
    
    // Find participations by status for an event
    List<EventParticipation> findByEventIdAndStatus(Long eventId, EventParticipation.ParticipationStatus status);
    
    // Count participations by status for an event
    @Query("SELECT COUNT(ep) FROM EventParticipation ep WHERE ep.eventId = ?1 AND ep.status = ?2")
    Long countByEventIdAndStatus(Long eventId, EventParticipation.ParticipationStatus status);
    
    // Count total participations for an event
    @Query("SELECT COUNT(ep) FROM EventParticipation ep WHERE ep.eventId = ?1")
    Long countByEventId(Long eventId);
    
    // Find participations for events created by a user
    @Query("SELECT ep FROM EventParticipation ep JOIN Event e ON ep.eventId = e.id WHERE e.createdByUsername = ?1")
    List<EventParticipation> findByEventCreator(String creatorUsername);
    
    // Delete participation
    void deleteByEventIdAndParticipantUsername(Long eventId, String participantUsername);
} 