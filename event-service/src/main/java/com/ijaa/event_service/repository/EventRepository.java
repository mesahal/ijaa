package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // Search events by location
    List<Event> findByLocationContainingIgnoreCaseAndActiveTrue(String location);
    
    // Search events by organizer name
    List<Event> findByOrganizerNameContainingIgnoreCaseAndActiveTrue(String organizerName);
    
    // Search events by title
    List<Event> findByTitleContainingIgnoreCaseAndActiveTrue(String title);
    
    // Search events by description
    List<Event> findByDescriptionContainingIgnoreCaseAndActiveTrue(String description);
    
    // Search events by online status
    List<Event> findByIsOnlineAndActiveTrue(Boolean isOnline);
    
    // Search events by privacy setting
    List<Event> findByPrivacyAndActiveTrue(Event.EventPrivacy privacy);
    
    // Complex search query
    @Query("SELECT e FROM Event e WHERE e.active = true " +
           "AND (:location IS NULL OR e.location LIKE %:location%) " +
           "AND (:eventType IS NULL OR e.eventType = :eventType) " +
           "AND (:startDate IS NULL OR e.startDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.endDate <= :endDate) " +
           "AND (:isOnline IS NULL OR e.isOnline = :isOnline) " +
           "AND (:organizerName IS NULL OR e.organizerName LIKE %:organizerName%) " +
           "AND (:title IS NULL OR e.title LIKE %:title%) " +
           "AND (:description IS NULL OR e.description LIKE %:description%) " +
           "ORDER BY e.startDate ASC")
    List<Event> searchEvents(@Param("location") String location, 
                           @Param("eventType") String eventType, 
                           @Param("startDate") LocalDateTime startDate, 
                           @Param("endDate") LocalDateTime endDate, 
                           @Param("isOnline") Boolean isOnline, 
                           @Param("organizerName") String organizerName, 
                           @Param("title") String title, 
                           @Param("description") String description);

    // Advanced search methods for new features
    // Find active events with pagination
    @Query("SELECT e FROM Event e WHERE e.active = true")
    Page<Event> findActiveEvents(Pageable pageable);

    // Find active events ordered by creation date
    @Query("SELECT e FROM Event e WHERE e.active = true ORDER BY e.createdAt DESC")
    Page<Event> findActiveEventsOrderByCreatedAtDesc(Pageable pageable);

    // Find active events ordered by current participants
    @Query("SELECT e FROM Event e WHERE e.active = true ORDER BY e.currentParticipants DESC")
    Page<Event> findActiveEventsOrderByCurrentParticipantsDesc(Pageable pageable);

    // Find events by location with pagination
    List<Event> findByLocationContainingAndActiveTrueOrderByStartDateAsc(String location, Pageable pageable);

    // Find events by organizer with pagination
    List<Event> findByOrganizerNameContainingAndActiveTrueOrderByStartDateDesc(String organizerName, Pageable pageable);

    // Find upcoming events
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.startDate > ?1 ORDER BY e.startDate ASC")
    Page<Event> findByStartDateAfterAndActiveTrueOrderByStartDateAsc(LocalDateTime startDate, Pageable pageable);

    // Find similar events
    List<Event> findByEventTypeAndLocationAndActiveTrueAndIdNot(String eventType, String location, Long eventId, Pageable pageable);

    // Additional methods for advanced search functionality
    
    // Find events by location containing (case insensitive)
    @Query("SELECT e FROM Event e WHERE e.active = true AND LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')) ORDER BY e.startDate ASC")
    Page<Event> findByLocationContainingIgnoreCaseAndActiveTrueOrderByStartDateAsc(@Param("location") String location, Pageable pageable);

    // Find events by organizer name containing (case insensitive)
    @Query("SELECT e FROM Event e WHERE e.active = true AND LOWER(e.organizerName) LIKE LOWER(CONCAT('%', :organizerName, '%')) ORDER BY e.startDate DESC")
    Page<Event> findByOrganizerNameContainingIgnoreCaseAndActiveTrueOrderByStartDateDesc(@Param("organizerName") String organizerName, Pageable pageable);

    // Find events with high engagement (high participant count)
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.currentParticipants >= :minParticipants ORDER BY e.currentParticipants DESC")
    Page<Event> findByCurrentParticipantsGreaterThanEqualAndActiveTrueOrderByCurrentParticipantsDesc(@Param("minParticipants") Integer minParticipants, Pageable pageable);

    // Find events by event type and active status
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.eventType = :eventType ORDER BY e.startDate ASC")
    Page<Event> findByEventTypeAndActiveTrueOrderByStartDateAsc(@Param("eventType") String eventType, Pageable pageable);

    // Find events by privacy setting and active status
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.privacy = :privacy ORDER BY e.startDate ASC")
    Page<Event> findByPrivacyAndActiveTrueOrderByStartDateAsc(@Param("privacy") Event.EventPrivacy privacy, Pageable pageable);

    // Find online events
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.isOnline = true ORDER BY e.startDate ASC")
    Page<Event> findByIsOnlineTrueAndActiveTrueOrderByStartDateAsc(Pageable pageable);

    // Find offline events
    @Query("SELECT e FROM Event e WHERE e.active = true AND e.isOnline = false ORDER BY e.startDate ASC")
    Page<Event> findByIsOnlineFalseAndActiveTrueOrderByStartDateAsc(Pageable pageable);
} 