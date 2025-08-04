package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    // Find all active events
    List<Event> findByActiveTrue();
    
    // Find all active events ordered by start date
    List<Event> findByActiveTrueOrderByStartDateAsc();
    
    // Find events by type
    List<Event> findByEventType(String eventType);
    
    // Find active events by type
    List<Event> findByEventTypeAndActiveTrue(String eventType);
    
    // Find events by organizer email
    List<Event> findByOrganizerEmail(String organizerEmail);
    
    // Find active events by organizer email
    List<Event> findByOrganizerEmailAndActiveTrue(String organizerEmail);
    
    // Find events happening between dates
    @Query("SELECT e FROM Event e WHERE e.startDate >= ?1 AND e.endDate <= ?2")
    List<Event> findEventsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find active events happening between dates
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.startDate >= ?1 AND e.endDate <= ?2")
    List<Event> findActiveEventsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    // User-specific queries
    // Find all events created by a specific user
    List<Event> findByCreatedByUsername(String username);
    
    // Find active events created by a specific user
    List<Event> findByCreatedByUsernameAndActiveTrue(String username);
    
    // Find events created by a specific user ordered by creation date
    List<Event> findByCreatedByUsernameOrderByCreatedAtDesc(String username);
    
    // Find active events created by a specific user ordered by start date
    List<Event> findByCreatedByUsernameAndActiveTrueOrderByStartDateAsc(String username);
    
    // Dashboard statistics methods
    @Query("SELECT COUNT(e) FROM Event e WHERE e.active = true")
    Long countByActiveTrue();
    
    @Query("SELECT COUNT(e) FROM Event e WHERE e.active = false")
    Long countByActiveFalse();
    
    @Query("SELECT COUNT(e) FROM Event e WHERE e.active = true AND e.startDate >= ?1")
    Long countActiveEventsFromDate(LocalDateTime fromDate);
} 